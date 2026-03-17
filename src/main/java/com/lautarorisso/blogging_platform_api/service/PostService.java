package com.lautarorisso.blogging_platform_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lautarorisso.blogging_platform_api.dto.PostRequest;
import com.lautarorisso.blogging_platform_api.model.PostEntity;
import com.lautarorisso.blogging_platform_api.model.UserEntity;
import com.lautarorisso.blogging_platform_api.repository.PostRepository;
import com.lautarorisso.blogging_platform_api.repository.UserRepository;
import com.lautarorisso.blogging_platform_api.security.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostEntity> getAllPosts(String term) {
        if (term == null || term.isBlank()) {
            return postRepository.findAll();
        }
        return postRepository.search(term);
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    public PostEntity createPost(PostEntity post) {
        return postRepository.save(post);
    }

    public PostEntity updatePost(Long id, PostRequest request, String username) {
        PostEntity post = getPostById(id);
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (!post.getAuthor().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this post");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setTags(request.getTags());
        return postRepository.save(post);
    }

    public void deletePost(Long id, String username) {
        PostEntity post = getPostById(id);
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (!post.getAuthor().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this post");
        }
        postRepository.deleteById(id);
    }

}
