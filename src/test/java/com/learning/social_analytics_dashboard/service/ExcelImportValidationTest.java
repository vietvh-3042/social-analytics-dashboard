package com.learning.social_analytics_dashboard.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

import com.learning.social_analytics_dashboard.messaging.ImportMessagePublisher;
import com.learning.social_analytics_dashboard.repository.ImportBatchRepository;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ExcelImportValidationTest {
    @Mock PostRepository postRepository;
    @Mock ImportBatchRepository batchRepository;
    @Mock ExcelReflectionMapper reflectionMapper;
    @Mock ImportMessagePublisher messagePublisher;

    @Test
    void rejectsNonXlsxBeforeOpeningWorkbook() {
        var service = new ExcelImportService(postRepository, batchRepository, reflectionMapper, messagePublisher);
        var file = new MockMultipartFile("file", "posts.csv", "text/csv", "content".getBytes());

        assertThatThrownBy(() -> service.importPosts(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only .xlsx workbooks are supported");
        verifyNoInteractions(batchRepository, postRepository, messagePublisher);
    }
}
