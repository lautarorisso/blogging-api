package com.lautarorisso.blogging_platform_api.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.lautarorisso.blogging_platform_api.dto.PostRequest;
import com.lautarorisso.blogging_platform_api.dto.PostResponse;
import com.lautarorisso.blogging_platform_api.model.PostEntity;
import com.lautarorisso.blogging_platform_api.model.TagEntity;

public class PostMapper {

    public static PostEntity toEntity(PostRequest request) {
        PostEntity post = new PostEntity();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        Set<TagEntity> tags = request.getTags().stream()
                .map(tag -> {
                    TagEntity t = new TagEntity();
                    t.setName(tag.getName());
                    return t;
                })
                .collect(Collectors.toSet());
        post.setTags(tags);
        return post;
    }

    public static PostResponse toPostResponse(PostEntity post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tags(post.getTags().stream().map(TagEntity::getName).collect(Collectors.toSet()))
                .build();
    }
}
