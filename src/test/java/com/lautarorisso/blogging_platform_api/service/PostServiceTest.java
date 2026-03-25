package com.lautarorisso.blogging_platform_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PageMapper pageMapper;
    @InjectMocks
    private PostService postService;

    private PostEntity post1;
    private PostEntity post2;
    private PostResponse postResponse1;
    private PostResponse postResponse2;
    private UserEntity lautaro;
    private UserEntity regularUser;

    @BeforeEach
    void setUp() {
        Role ADMIN = Role.ADMIN;
        lautaro = new UserEntity(1L, "LautaroTest", "password", ADMIN, null);
        regularUser = new UserEntity(2L, "RegularUser", "password", Role.USER, null);
        post1 = new PostEntity(1L, "Test Title", "Test Content", "Test Category", null, null, null, lautaro);
        post1.setTags(new HashSet<>());
        post2 = new PostEntity(2L, "Title 2", "Content 2", "Category 2", null, null, null, lautaro);
        post2.setTags(new HashSet<>());
        postResponse1 = PostMapper.toPostResponse(post1);
        postResponse2 = PostMapper.toPostResponse(post2);
    }

    @Test
    @SuppressWarnings("null")
    void testGetAllPostsWithTerm() {
        Pageable pageable = PageRequest.of(0, 10);
        String term = "test";
        PageImpl<PostEntity> postPage = new PageImpl<>(List.of(post1), pageable, 1);

        when(postRepository.findByTitleContainingIgnoreCase(term, pageable))
                .thenReturn(postPage);
        when(pageMapper.toPageResponse(any(), any()))
                .thenReturn(new PageResponse<>(List.of(postResponse1), 1, 1, 0, 10));

        PageResponse<PostResponse> result = postService.getAllPosts(pageable, term);

        assertNotNull(result);
        assertEquals(postResponse1, result.getContent().get(0));

        verify(postRepository, times(1)).findByTitleContainingIgnoreCase(term, pageable);
        verify(pageMapper, times(1)).toPageResponse(any(), any());
    }

    @Test
    @SuppressWarnings("null")
    void testGetAllPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<PostEntity> postPage = new PageImpl<>(List.of(post1, post2), pageable, 2);

        when(postRepository.findAll(pageable)).thenReturn(postPage);
        when(pageMapper.toPageResponse(any(), any()))
                .thenReturn(new PageResponse<>(List.of(postResponse1, postResponse2), 2, 1, 0, 10));

        PageResponse<PostResponse> result = postService.getAllPosts(pageable, null);

        assertNotNull(result);
        assertEquals(List.of(postResponse1, postResponse2), result.getContent());

        verify(postRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetPostByIdSuccesfully() {
        Long id = 1L;

        when(postRepository.findById(id)).thenReturn(Optional.of(post1));

        PostEntity result = postService.getPostById(id);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("Test Category", result.getCategory());

        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void testGetPostByIdException() {
        Long id = 999L;

        when(postRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPostById(id);
        });

        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void testCreatePostSuccessfully() {
        PostEntity newPost = new PostEntity(null, "New Title", "New Content", "New Category", null, null, null,
                lautaro);

        when(postRepository.save(newPost))
                .thenReturn(new PostEntity(3L, "New Title", "New Content", "New Category", null, null, null, lautaro));

        PostEntity result = postService.createPost(newPost);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
        assertEquals("New Category", result.getCategory());

        verify(postRepository, times(1)).save(newPost);
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostSuccessfully() {
        Long id = 1L;
        PostRequest request = new PostRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        request.setCategory("Updated Category");
        request.setTags(null);

        when(postRepository.findById(id)).thenReturn(Optional.of(post1));
        when(userRepository.findByUsername("LautaroTest")).thenReturn(Optional.of(lautaro));
        when(postRepository.save(any(PostEntity.class))).thenReturn(post1);

        PostEntity result = postService.updatePost(id, request, "LautaroTest");

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertEquals("Updated Category", result.getCategory());

        verify(postRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByUsername("LautaroTest");
        verify(postRepository, times(1)).save(any(PostEntity.class));
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostForbidden() {
        Long id = 1L;
        PostRequest request = new PostRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        request.setCategory("Updated Category");
        request.setTags(null);

        when(postRepository.findById(id)).thenReturn(Optional.of(post1));
        when(userRepository.findByUsername("RegularUser")).thenReturn(Optional.of(regularUser));

        assertThrows(ForbiddenException.class, () -> {
            postService.updatePost(id, request, "RegularUser");
        });

        verify(postRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByUsername("RegularUser");
        verify(postRepository, times(0)).save(any(PostEntity.class));
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostUserNotFound() {
        Long id = 1L;
        PostRequest request = new PostRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        request.setCategory("Updated Category");
        request.setTags(null);

        when(postRepository.findById(id)).thenReturn(Optional.of(post1));
        when(userRepository.findByUsername("NonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            postService.updatePost(id, request, "NonExistentUser");
        });

        verify(postRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByUsername("NonExistentUser");
        verify(postRepository, times(0)).save(any(PostEntity.class));
    }

    @Test
    void testDeletePostSuccessfully() {
        Long id = 1L;

        when(postRepository.findById(id)).thenReturn(Optional.of(post1));
        when(userRepository.findByUsername("LautaroTest")).thenReturn(Optional.of(lautaro));

        postService.deletePost(id, "LautaroTest");

        verify(postRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByUsername("LautaroTest");
        verify(postRepository, times(1)).deleteById(id);
    }

}