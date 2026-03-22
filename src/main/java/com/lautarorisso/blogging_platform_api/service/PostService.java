package com.lautarorisso.blogging_platform_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.lautarorisso.blogging_platform_api.dto.PageResponse;
import com.lautarorisso.blogging_platform_api.dto.PostRequest;
import com.lautarorisso.blogging_platform_api.dto.PostResponse;
import com.lautarorisso.blogging_platform_api.exception.ForbiddenException;
import com.lautarorisso.blogging_platform_api.exception.ResourceNotFoundException;
import com.lautarorisso.blogging_platform_api.exception.UserNotFoundException;
import com.lautarorisso.blogging_platform_api.mapper.PageMapper;
import com.lautarorisso.blogging_platform_api.mapper.PostMapper;
import com.lautarorisso.blogging_platform_api.model.PostEntity;
import com.lautarorisso.blogging_platform_api.model.UserEntity;
import com.lautarorisso.blogging_platform_api.repository.PostRepository;
import com.lautarorisso.blogging_platform_api.repository.UserRepository;
import com.lautarorisso.blogging_platform_api.security.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PageMapper pageMapper;

    public PageResponse<PostResponse> getAllPosts(Pageable pageable, String term) {
        Page<PostEntity> postPage;
        if (term == null || term.isBlank()) {
            postPage = postRepository.findAll(pageable);
        } else {
            postPage = postRepository.findByTitleContainingIgnoreCase(term, pageable);
        }
        return pageMapper.toPageResponse(postPage, PostMapper::toPostResponse);
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    public PostEntity createPost(PostEntity post) {
        log.info("Creating post with title: {}", post.getTitle());
        return postRepository.save(post);
    }

    public PostEntity updatePost(Long id, PostRequest request, String username) {
        log.info("Updating post with id: {}", id);
        PostEntity post = getPostById(id);
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!post.getAuthor().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You are not allowed to update this post");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setTags(request.getTags());
        return postRepository.save(post);
    }

    public void deletePost(Long id, String username) {
        log.info("Deleting post with id: {}", id);
        PostEntity post = getPostById(id);
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!post.getAuthor().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You are not allowed to delete this post");
        }
        postRepository.deleteById(id);
    }

}
