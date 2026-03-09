package com.lautarorisso.blogging_platform_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lautarorisso.blogging_platform_api.model.Post;
import com.lautarorisso.blogging_platform_api.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post updatedPost) {
        Post post = getPostById(id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setCategory(updatedPost.getCategory());
        post.setTags(updatedPost.getTags());
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

}
