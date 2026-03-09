package com.lautarorisso.blogging_platform_api.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.lautarorisso.blogging_platform_api.dto.PostRequest;
import com.lautarorisso.blogging_platform_api.dto.PostResponse;
import com.lautarorisso.blogging_platform_api.model.Post;
import com.lautarorisso.blogging_platform_api.model.Tag;

public class PostMapper {

    public static Post toEntity(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        Set<Tag> tags = request.getTags().stream()
                .map(tag -> {
                    Tag t = new Tag();
                    t.setName(tag.getName());
                    return t;
                })
                .collect(Collectors.toSet());
        post.setTags(tags);
        return post;
    }

    public static PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }
}
