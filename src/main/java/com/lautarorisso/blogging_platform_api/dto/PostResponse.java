package com.lautarorisso.blogging_platform_api.dto;

import java.util.Set;

import com.lautarorisso.blogging_platform_api.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Set<String> tags;
    private User author;
}
