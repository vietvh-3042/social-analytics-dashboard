package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExcelExportService implements PostExportUseCase {
    private final PostRepository postRepository;

    @Override
    public byte[] exportReport() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("posts");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("externalId");
            header.createCell(1).setCellValue("source");
            header.createCell(2).setCellValue("content");
            header.createCell(3).setCellValue("publishedAt");
            int rowNumber = 1;
            for (PostEntity post : postRepository.findAllByOrderByPublishedAtDesc()) {
                var row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(post.getExternalId() == null ? "" : post.getExternalId());
                row.createCell(1).setCellValue(post.getSource().name());
                row.createCell(2).setCellValue(post.getContent());
                row.createCell(3).setCellValue(post.getPublishedAt().toString());
            }
            workbook.write(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not export workbook", ex);
        }
    }
}
