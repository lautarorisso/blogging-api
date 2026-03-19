package com.lautarorisso.blogging_platform_api.mapper;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.lautarorisso.blogging_platform_api.dto.PageResponse;

@Component
public class PageMapper {
    public <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        List<R> content = page.getContent().stream()
                .map(mapper)
                .toList();
        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(),
                page.getSize());
    }
}
