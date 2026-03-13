package com.lautarorisso.blogging_platform_api.dto;

import java.util.Set;

import com.lautarorisso.blogging_platform_api.model.Tag;

import lombok.Data;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String category;
    private Set<Tag> tags;
}
