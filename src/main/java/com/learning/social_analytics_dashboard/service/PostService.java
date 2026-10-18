package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.request.PostRequest;
import com.learning.social_analytics_dashboard.dto.response.PostResponse;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.exception.NotFoundException;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {
    private final PostRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<PostResponse> list() {
        return repository.findAllByOrderByPublishedAtDesc().stream().map(PostResponse::from).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PostEntity get(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Post not found: " + id));
    }

    @Transactional
    @Override
    public PostResponse create(PostRequest request) {
        PostEntity post = new PostEntity(request.source(), request.externalId(), request.content(), request.publishedAt());
        return PostResponse.from(repository.save(post));
    }

    @Transactional
    @Override
    public PostResponse update(UUID id, PostRequest request) {
        PostEntity post = get(id);
        post.setContent(request.content());
        post.setPublishedAt(request.publishedAt());
        return PostResponse.from(repository.save(post));
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Post not found: " + id);
        }
        repository.deleteById(id);
    }
}
