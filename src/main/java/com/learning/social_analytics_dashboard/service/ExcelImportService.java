package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.dto.request.ExcelPostRow;
import com.learning.social_analytics_dashboard.dto.response.ImportResult;
import com.learning.social_analytics_dashboard.entity.ImportBatchEntity;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.messaging.ImportCompletedEvent;
import com.learning.social_analytics_dashboard.messaging.ImportMessagePublisher;
import com.learning.social_analytics_dashboard.repository.ImportBatchRepository;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelImportService implements PostImportUseCase {
    private static final long MAX_FILE_BYTES = 10 * 1024 * 1024;
    private static final int MAX_ROWS = 10_000;
    private final PostRepository postRepository;
    private final ImportBatchRepository batchRepository;
    private final ExcelReflectionMapper reflectionMapper;
    private final ImportMessagePublisher messagePublisher;

    @Transactional
    @Override
    public ImportResult importPosts(MultipartFile file) {
        validateFile(file);
        ImportBatchEntity batch = batchRepository.save(new ImportBatchEntity(file.getOriginalFilename()));
        List<String> diagnostics = new ArrayList<>();
        int imported = 0;
        int rejected = 0;
        try (InputStream input = file.getInputStream(); Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() > MAX_ROWS) {
                throw new IllegalArgumentException("Workbook exceeds the maximum row count");
            }
            Map<Integer, String> headers = headers(sheet.getRow(0));
            for (int index = 1; index <= sheet.getLastRowNum(); index++) {
                try {
                    ExcelPostRow row = reflectionMapper.map(values(sheet.getRow(index), headers), ExcelPostRow.class);
                    if (row.getContent() == null || row.getContent().isBlank()) throw new IllegalArgumentException("content is required");
                    Instant publishedAt = row.getPublishedAt() == null || row.getPublishedAt().isBlank()
                            ? Instant.now() : Instant.parse(row.getPublishedAt());
                    if (row.getExternalId() != null && postRepository.findBySourceAndExternalId(SourcePlatform.IMPORT, row.getExternalId()).isPresent()) {
                        throw new IllegalArgumentException("duplicate externalId");
                    }
                    postRepository.save(new PostEntity(SourcePlatform.IMPORT, row.getExternalId(), row.getContent(), publishedAt));
                    imported++;
                } catch (RuntimeException ex) {
                    rejected++;
                    diagnostics.add("Row " + (index + 1) + ": " + ex.getMessage());
                }
            }
            batch.complete(imported, rejected);
            messagePublisher.publish(new ImportCompletedEvent(batch.getId(), imported, batch.getCompletedAt()));
            return new ImportResult(batch.getId(), imported, rejected, diagnostics);
        } catch (IOException | RuntimeException ex) {
            batch.complete(imported, rejected + 1);
            throw new IllegalArgumentException("Workbook could not be read", ex);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Workbook is empty");
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
            throw new IllegalArgumentException("Only .xlsx workbooks are supported");
        }
        if (file.getSize() > MAX_FILE_BYTES) throw new IllegalArgumentException("Workbook is too large");
    }

    private Map<Integer, String> headers(Row headerRow) {
        if (headerRow == null) throw new IllegalArgumentException("Workbook header is missing");
        Map<Integer, String> headers = new HashMap<>();
        DataFormatter formatter = new DataFormatter();
        headerRow.forEach(cell -> headers.put(cell.getColumnIndex(), formatter.formatCellValue(cell).trim().toLowerCase(Locale.ROOT)));
        if (!headers.containsValue("content")) throw new IllegalArgumentException("content header is required");
        return headers;
    }

    private Map<String, String> values(Row row, Map<Integer, String> headers) {
        if (row == null) throw new IllegalArgumentException("row is empty");
        DataFormatter formatter = new DataFormatter();
        Map<String, String> values = new HashMap<>();
        row.forEach(cell -> values.put(headers.get(cell.getColumnIndex()), formatter.formatCellValue(cell).trim()));
        return values;
    }
}
