package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import com.learning.social_analytics_dashboard.dto.request.PostRequest;
import com.learning.social_analytics_dashboard.dto.response.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository repository;

    @InjectMocks
    private PostService service;

    @Test
    void createsPostFromValidatedRequest() {
        PostRequest request = new PostRequest(SourcePlatform.IMPORT, "ext-1", "hello", Instant.parse("2026-01-01T00:00:00Z"));
        when(repository.save(any(PostEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostResponse response = service.create(request);

        assertThat(response.externalId()).isEqualTo("ext-1");
        assertThat(response.source()).isEqualTo(SourcePlatform.IMPORT);
        assertThat(response.content()).isEqualTo("hello");
    }

    @Test
    void deletesOnlyWhenPostExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        org.mockito.Mockito.verify(repository).deleteById(id);
    }
}
