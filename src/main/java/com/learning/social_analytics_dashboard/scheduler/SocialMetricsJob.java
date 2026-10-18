package com.learning.social_analytics_dashboard.scheduler;

import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocialMetricsJob {
    private final PostRepository postRepository;
    private final SocialMetricsUpdateService updateService;

    @Scheduled(cron = "${app.metrics.schedule}")
    public void updateSocialMetricsJob() {
        for (PostEntity post : postRepository.findAll()) {
            try {
                updateService.update(post);
            } catch (RuntimeException ex) {
                log.warn("Could not schedule metrics update for post {}", post.getId(), ex);
            }
        }
    }
}
