package com.lautarorisso.blogging_platform_api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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
import org.springframework.data.domain.Sort;

import com.lautarorisso.blogging_platform_api.dto.PageResponse;
import com.lautarorisso.blogging_platform_api.dto.PostRequest;
import com.lautarorisso.blogging_platform_api.dto.PostResponse;
import com.lautarorisso.blogging_platform_api.mapper.PostMapper;
import com.lautarorisso.blogging_platform_api.model.PostEntity;
import com.lautarorisso.blogging_platform_api.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.lautarorisso.blogging_platform_api.dto.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Posts", description = "Post management APIs")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
public class PostController {
    private final PostService postService;

    @Operation(summary = "Get all posts", description = "Retrieves a paginated list of posts, optionally filtered by search term", security = {}, tags = {
            "Posts" })
    @ApiResponse(responseCode = "200", description = "List of posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class)))
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<PageResponse<PostResponse>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @NonNull Pageable pageable,
            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(postService.getAllPosts(pageable, term));
    }

    @Operation(summary = "Get post by ID", description = "Retrieves a single post by its ID", security = {}, tags = {
            "Posts" })
    @ApiResponse(responseCode = "200", description = "Post retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<PostResponse> getPostById(@PathVariable @NonNull Long id) {
        PostEntity post = postService.getPostById(id);
        return ResponseEntity.ok(PostMapper.toPostResponse(post));
    }

    @Operation(summary = "Create a new post", description = "Creates a new post with the provided details", tags = {
            "Posts" })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Post creation details", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostRequest.class)))
    @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid post data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - not authorized to create posts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        PostEntity saved = postService.createPost(PostMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toPostResponse(saved));
    }

    @Operation(summary = "Update an existing post", description = "Updates an existing post with the provided details", tags = {
            "Posts" })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Post update details", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostRequest.class)))
    @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid post data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - not authorized to update this post", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<PostResponse> updatePost(@Valid @PathVariable @NonNull Long id,
            @RequestBody PostRequest request,
            Authentication authentication) {
        PostEntity updated = postService.updatePost(id, request, authentication.getPrincipal().toString());
        return ResponseEntity.ok(PostMapper.toPostResponse(updated));
    }

    @Operation(summary = "Delete a post", description = "Deletes an existing post by its ID", tags = {
            "Posts" })
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - not authorized to delete this post", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@Valid @PathVariable @NonNull Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getPrincipal().toString());
        return ResponseEntity.noContent().build();
    }
}
