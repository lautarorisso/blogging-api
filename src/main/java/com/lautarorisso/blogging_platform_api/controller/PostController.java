package com.lautarorisso.blogging_platform_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lautarorisso.blogging_platform_api.dto.PostRequest;
import com.lautarorisso.blogging_platform_api.dto.PostResponse;
import com.lautarorisso.blogging_platform_api.mapper.PostMapper;
import com.lautarorisso.blogging_platform_api.model.PostEntity;
import com.lautarorisso.blogging_platform_api.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
@Valid
public class PostController {
    private final PostService postService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(required = false) String term) {
        List<PostEntity> posts = postService.getAllPosts(term);
        return ResponseEntity.ok(posts.stream().map(PostMapper::toPostResponse).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostEntity post = postService.getPostById(id);
        return ResponseEntity.ok(PostMapper.toPostResponse(post));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        PostEntity saved = postService.createPost(PostMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toPostResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<PostResponse> updatePost(@Valid @PathVariable Long id, @RequestBody PostRequest request,
            Authentication authentication) {
        PostEntity updated = postService.updatePost(id, request, authentication.getName());
        return ResponseEntity.ok(PostMapper.toPostResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@Valid @PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
