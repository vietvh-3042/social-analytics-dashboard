package com.learning.socialanalytics.service;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.ChartPointDto;
import com.learning.socialanalytics.dto.MetricDto;
import com.learning.socialanalytics.dto.SyncMessage;
import com.learning.socialanalytics.dto.SyncRunDto;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.entity.CrawlSetting;
import com.learning.socialanalytics.entity.MetricSnapshot;
import com.learning.socialanalytics.entity.SocialPost;
import com.learning.socialanalytics.entity.SyncRun;
import com.learning.socialanalytics.repository.AdminUserRepository;
import com.learning.socialanalytics.repository.CrawlSettingRepository;
import com.learning.socialanalytics.repository.MetricSnapshotRepository;
import com.learning.socialanalytics.repository.SocialPostRepository;
import com.learning.socialanalytics.repository.SyncRunRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.data.domain.PageRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class MetricSyncService {
  private final AdminUserRepository adminRepository;
  private final CrawlSettingRepository settingRepository;
  private final MetricSnapshotRepository metricRepository;
  private final SocialPostRepository postRepository;
  private final SyncRunRepository runRepository;
  private final JmsTemplate jmsTemplate;
  private final SimpMessagingTemplate messagingTemplate;

  @Transactional
  public SyncRunDto syncNow(String facebookSubject) {
    AdminUser admin =
        adminRepository
            .findByFacebookSubject(facebookSubject)
            .orElseThrow(() -> new IllegalStateException("Signed-in user is missing"));
    return enqueue(admin, ApplicationConstants.SOURCE_MANUAL);
  }

  @Transactional
  public int enqueueDueRuns() {
    Instant now = Instant.now();
    List<CrawlSetting> dueSettings = settingRepository.findDue(now);
    for (CrawlSetting setting : dueSettings) {
      AdminUser admin = adminRepository.findById(setting.getAdminId()).orElse(null);
      if (admin == null) {
        log.warn("Skipping crawl setting for missing admin {}", setting.getAdminId());
        continue;
      }
      enqueue(admin, ApplicationConstants.SOURCE_SCHEDULED);
      setting.markRun(now);
    }
    return dueSettings.size();
  }

  private SyncRunDto enqueue(AdminUser admin, String source) {
    SyncRun run = runRepository.save(new SyncRun(admin, source));
    UUID runId = run.getId();
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            jmsTemplate.convertAndSend(
                ApplicationConstants.METRIC_SYNC_QUEUE, new SyncMessage(runId));
          }
        });
    return toDto(run);
  }

  @Transactional
  public void execute(UUID runId) {
    SyncRun run =
        runRepository
            .findById(runId)
            .orElseThrow(() -> new IllegalStateException("Sync run does not exist"));
    if (ApplicationConstants.STATUS_COMPLETED.equals(run.getStatus())) {
      return;
    }
    AdminUser admin = run.getAdmin();
    Instant capturedAt = Instant.now();
    List<SocialPost> posts = postRepository.findActiveByOwner(admin.getId());
    Faker faker = new Faker();
    List<MetricSnapshot> snapshots =
        posts.stream()
            .map(
                post ->
                    new MetricSnapshot(
                        post,
                        faker.number().numberBetween(50, 25000),
                        faker.number().numberBetween(0, 1500),
                        faker.number().numberBetween(0, 900),
                        faker.number().numberBetween(1000, 500000),
                        capturedAt))
            .toList();
    metricRepository.saveAll(snapshots);
    run.complete(snapshots.size(), capturedAt);
    settingRepository
        .findByAdminId(admin.getId())
        .ifPresent(setting -> setting.markRun(capturedAt));
    SyncRunDto event =
        new SyncRunDto(
            run.getId(),
            run.getSource(),
            ApplicationConstants.STATUS_COMPLETED,
            snapshots.size(),
            run.getStartedAt(),
            capturedAt,
            null);
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            messagingTemplate.convertAndSendToUser(
                admin.getFacebookSubject(),
                ApplicationConstants.WEBSOCKET_METRICS_DESTINATION,
                event);
          }
        });
  }

  @Transactional
  public void markFailed(UUID runId) {
    runRepository
        .findById(runId)
        .ifPresent(run -> run.fail("Metric generation failed", Instant.now()));
  }

  @Transactional(readOnly = true)
  public List<MetricDto> history(String facebookSubject) {
    AdminUser admin =
        adminRepository
            .findByFacebookSubject(facebookSubject)
            .orElseThrow(() -> new IllegalStateException("Signed-in user is missing"));
    return metricRepository
        .findRecent(admin.getId(), PageRequest.of(0, ApplicationConstants.MAX_METRIC_SNAPSHOTS))
        .stream()
        .map(
            snapshot ->
                new MetricDto(
                    snapshot.getId(),
                    snapshot.getPost().getId(),
                    snapshot.getPost().getTitle(),
                    snapshot.getLikes(),
                    snapshot.getComments(),
                    snapshot.getShares(),
                    snapshot.getFollowers(),
                    snapshot.getCapturedAt(),
                    true))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<SyncRunDto> runs(String facebookSubject) {
    AdminUser admin =
        adminRepository
            .findByFacebookSubject(facebookSubject)
            .orElseThrow(() -> new IllegalStateException("Signed-in user is missing"));
    return runRepository
        .findRecentByAdmin(admin.getId(), PageRequest.of(0, ApplicationConstants.MAX_SYNC_RUNS))
        .stream()
        .map(MetricSyncService::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ChartPointDto> chartData(String facebookSubject, int days) {
    AdminUser admin =
        adminRepository
            .findByFacebookSubject(facebookSubject)
            .orElseThrow(() -> new IllegalStateException("Signed-in user is missing"));
    Instant since = Instant.now().minusSeconds(Math.max(1, Math.min(days, 365)) * 86400L);
    Map<LocalDate, Map<UUID, MetricSnapshot>> latestByPostAndDay = new TreeMap<>();
    metricRepository
        .findForChart(admin.getId(), since)
        .forEach(
            snapshot -> {
              LocalDate day = snapshot.getCapturedAt().atZone(ZoneOffset.UTC).toLocalDate();
              latestByPostAndDay
                  .computeIfAbsent(day, ignored -> new java.util.HashMap<>())
                  .put(snapshot.getPost().getId(), snapshot);
            });
    return latestByPostAndDay.entrySet().stream()
        .map(
            entry -> {
              long[] values =
                  entry.getValue().values().stream()
                      .collect(
                          () -> new long[4],
                          (sum, metric) -> {
                            sum[0] += metric.getLikes();
                            sum[1] += metric.getComments();
                            sum[2] += metric.getShares();
                            sum[3] += metric.getFollowers();
                          },
                          (first, second) -> {
                            for (int index = 0; index < first.length; index++)
                              first[index] += second[index];
                          });
              return new ChartPointDto(
                  entry.getKey().atStartOfDay(ZoneOffset.UTC).toInstant(),
                  values[0],
                  values[1],
                  values[2],
                  values[3]);
            })
        .toList();
  }

  private static SyncRunDto toDto(SyncRun run) {
    return new SyncRunDto(
        run.getId(),
        run.getSource(),
        run.getStatus(),
        run.getSnapshotsCreated(),
        run.getStartedAt(),
        run.getCompletedAt(),
        run.getErrorSummary());
  }
}
