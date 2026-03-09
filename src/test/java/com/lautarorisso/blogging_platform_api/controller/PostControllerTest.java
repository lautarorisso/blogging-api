package com.lautarorisso.blogging_platform_api.controller;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lautarorisso.blogging_platform_api.model.Post;
import com.lautarorisso.blogging_platform_api.model.Tag;
import com.lautarorisso.blogging_platform_api.service.PostService;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    private Post createPost(Long id, String title, String content, String category) {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("java");
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setTags(Set.of(tag));
        return post;
    }

    @Test
    void getAllPosts_returnsAllPosts_whenNoSearchTerm() throws Exception {
        Post post1 = createPost(1L, "First Post", "Content 1", "Tech");
        Post post2 = createPost(2L, "Second Post", "Content 2", "Science");
        when(postService.getAllPosts(null)).thenReturn(List.of(post1, post2));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("First Post"))
                .andExpect(jsonPath("$[1].title").value("Second Post"));
    }

    @Test
    void getAllPosts_returnsMatchingPosts_whenSearchTermProvided() throws Exception {
        Post post = createPost(1L, "Spring Boot Guide", "Learn Spring Boot", "Tech");
        when(postService.getAllPosts("spring")).thenReturn(List.of(post));

        mockMvc.perform(get("/posts").param("term", "spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot Guide"));
    }

    @Test
    void getPostById_returns404_whenPostDoesNotExist() throws Exception {
        when(postService.getPostById(999L)).thenReturn(null);

        mockMvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPost_returns400_whenTitleIsMissing() throws Exception {
        String json = """
                {
                    "content": "Some content",
                    "tags": [{"name": "java"}]
                }
                """;

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePost_returns404_whenPostDoesNotExist() throws Exception {
        when(postService.getPostById(999L)).thenReturn(null);

        mockMvc.perform(delete("/posts/999"))
                .andExpect(status().isNotFound());
    }
}
