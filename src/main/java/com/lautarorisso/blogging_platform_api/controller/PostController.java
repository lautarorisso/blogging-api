package com.lautarorisso.blogging_platform_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.lautarorisso.blogging_platform_api.model.Post;
import com.lautarorisso.blogging_platform_api.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(required = false) String term) {
        List<Post> posts = postService.getAllPosts(term);
        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(posts.stream().map(PostMapper::toPostResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PostMapper.toPostResponse(post));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Post post = PostMapper.toEntity(request);
        Post saved = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toPostResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest updatedPost) {
        Post existing = postService.getPostById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        if (updatedPost.getTitle() == null || updatedPost.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Post post = PostMapper.toEntity(updatedPost);
        Post updated = postService.updatePost(id, post);
        return ResponseEntity.ok(PostMapper.toPostResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Post existing = postService.getPostById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
