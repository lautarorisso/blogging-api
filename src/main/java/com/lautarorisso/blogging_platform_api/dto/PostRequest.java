package com.lautarorisso.blogging_platform_api.dto;

import java.util.Set;

import com.lautarorisso.blogging_platform_api.model.TagEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank
    @Size(max = 150)
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String category;
    private Set<TagEntity> tags;
}
