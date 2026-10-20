package com.learning.socialanalytics.service;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.ImportCompletedMessage;
import com.learning.socialanalytics.dto.ImportResultDto;
import com.learning.socialanalytics.dto.ImportRowResultDto;
import com.learning.socialanalytics.dto.PostDto;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.entity.SocialPost;
import com.learning.socialanalytics.repository.SocialPostRepository;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostService {
    private final SocialPostRepository postRepository;
    private final JmsTemplate jmsTemplate;

    public PostService(SocialPostRepository postRepository, JmsTemplate jmsTemplate) {
        this.postRepository = postRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional(readOnly = true)
    public List<PostDto> list(AdminUser owner) {
        return postRepository.findAllByOwner_IdOrderByImportedAtDesc(owner.getId()).stream()
                .map(
                        post ->
                                new PostDto(
                                        post.getId(),
                                        post.getTitle(),
                                        post.getPostUrl(),
                                        post.getImportedAt()))
                .toList();
    }

    @Transactional
    public ImportResultDto importWorkbook(AdminUser owner, MultipartFile file) {
        if (file.isEmpty() || file.getSize() > ApplicationConstants.MAX_IMPORT_BYTES) {
            throw new IllegalArgumentException("Choose a non-empty .xlsx file up to 10 MB");
        }
        if (file.getOriginalFilename() == null
                || !file.getOriginalFilename()
                        .toLowerCase()
                        .endsWith(ApplicationConstants.XLSX_EXTENSION)) {
            throw new IllegalArgumentException("Only .xlsx workbooks are supported");
        }
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            return importSheet(owner, workbook.getSheetAt(0));
        } catch (IOException exception) {
            throw new IllegalArgumentException("The Excel workbook could not be read");
        }
    }

    private ImportResultDto importSheet(AdminUser owner, Sheet sheet) {
        if (sheet.getLastRowNum() > ApplicationConstants.MAX_IMPORT_ROWS) {
            throw new IllegalArgumentException("Workbook exceeds the 5,000 row limit");
        }
        DataFormatter formatter = new DataFormatter();
        Row header = sheet.getRow(0);
        if (header == null
                || !ApplicationConstants.REQUIRED_IMPORT_HEADER.equals(
                        formatter.formatCellValue(header.getCell(0)).trim().toLowerCase())) {
            throw new IllegalArgumentException(
                    "First column header must be " + ApplicationConstants.REQUIRED_IMPORT_HEADER);
        }
        List<ImportRowResultDto> results = new ArrayList<>();
        List<SocialPost> accepted = new ArrayList<>();
        Set<String> acceptedUrls = new HashSet<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            String url = row == null ? "" : formatter.formatCellValue(row.getCell(0)).trim();
            String title = row == null ? "" : formatter.formatCellValue(row.getCell(1)).trim();
            if (!isFacebookUrl(url)) {
                results.add(
                        new ImportRowResultDto(
                                rowIndex + 1, "REJECTED", "Enter a valid Facebook post URL"));
            } else if (postRepository.existsByOwner_IdAndPostUrl(owner.getId(), url)
                    || !acceptedUrls.add(url)) {
                results.add(
                        new ImportRowResultDto(
                                rowIndex + 1, "REJECTED", "This post URL is already imported"));
            } else {
                accepted.add(
                        new SocialPost(
                                owner,
                                url,
                                title.isBlank()
                                        ? ApplicationConstants.DEFAULT_SOCIAL_POST_TITLE
                                        : title));
                results.add(new ImportRowResultDto(rowIndex + 1, "ACCEPTED", "Post imported"));
            }
        }
        postRepository.saveAll(accepted);
        if (!accepted.isEmpty()) {
            ImportCompletedMessage event =
                    new ImportCompletedMessage(owner.getId(), accepted.size());
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            jmsTemplate.convertAndSend(
                                    ApplicationConstants.IMPORT_COMPLETED_QUEUE, event);
                        }
                    });
        }
        return new ImportResultDto(accepted.size(), results.size() - accepted.size(), results);
    }

    private boolean isFacebookUrl(String value) {
        try {
            URI uri = URI.create(value);
            String host = uri.getHost();
            return "https".equalsIgnoreCase(uri.getScheme())
                    && host != null
                    && (host.equalsIgnoreCase("facebook.com")
                            || host.endsWith(".facebook.com")
                            || host.equalsIgnoreCase("fb.watch"));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
