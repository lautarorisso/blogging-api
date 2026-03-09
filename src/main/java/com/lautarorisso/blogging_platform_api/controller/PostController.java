package com.lautarorisso.blogging_platform_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts().stream().map(PostMapper::toPostResponse).toList();
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return PostMapper.toPostResponse(post);
    }

    @PostMapping
    public PostResponse createPost(@RequestBody PostRequest request) {
        Post post = PostMapper.toEntity(request);
        Post saved = postService.createPost(post);
        return PostMapper.toPostResponse(saved);
    }

    @PutMapping("/{id}")
    public PostResponse updatePost(@PathVariable Long id, @RequestBody PostRequest updatedPost) {
        Post post = PostMapper.toEntity(updatedPost);
        Post updated = postService.updatePost(id, post);
        return PostMapper.toPostResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
