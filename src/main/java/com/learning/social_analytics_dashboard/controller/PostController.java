package com.learning.social_analytics_dashboard.controller;

import com.learning.social_analytics_dashboard.dto.request.PostRequest;
import com.learning.social_analytics_dashboard.dto.response.PostResponse;
import com.learning.social_analytics_dashboard.service.PostUseCase;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts")
public class PostController {
    private final PostUseCase service;

    @GetMapping
    @Operation(summary = "List posts ordered by publication time")
    public List<PostResponse> list() { return service.list(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get one post")
    public PostResponse get(@PathVariable UUID id) { return PostResponse.from(service.get(id)); }

    @PostMapping
    @Operation(summary = "Create a post")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    public PostResponse update(@PathVariable UUID id, @Valid @RequestBody PostRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { service.delete(id); }
}
