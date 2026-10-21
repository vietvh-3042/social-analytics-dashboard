package com.learning.socialanalytics.service;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.MetricExportRow;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.entity.MetricSnapshot;
import com.learning.socialanalytics.repository.MetricSnapshotRepository;
import com.learning.socialanalytics.util.SpreadsheetMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
  private final MetricSnapshotRepository metricRepository;

  @Transactional(readOnly = true)
  public byte[] exportMetrics(AdminUser admin) {
    List<MetricSnapshot> snapshots =
        metricRepository.findRecent(
            admin.getId(), PageRequest.of(0, ApplicationConstants.MAX_METRIC_SNAPSHOTS));
    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      XSSFSheet sheet = workbook.createSheet(ApplicationConstants.REPORT_SHEET_NAME);
      SpreadsheetMapper.writeHeaders(sheet.createRow(0), MetricExportRow.class);
      for (int index = 0; index < snapshots.size(); index++) {
        MetricSnapshot snapshot = snapshots.get(index);
        MetricExportRow exportRow =
            new MetricExportRow(
                snapshot.getPost().getTitle(),
                snapshot.getPost().getPostUrl(),
                snapshot.getLikes(),
                snapshot.getComments(),
                snapshot.getShares(),
                snapshot.getFollowers(),
                snapshot.getCapturedAt().toString(),
                "Synthetic / Datafaker");
        SpreadsheetMapper.writeValues(sheet.createRow(index + 1), exportRow);
      }
      workbook.write(output);
      return output.toByteArray();
    } catch (IOException exception) {
      throw new IllegalStateException("Report could not be generated");
    }
  }
}
