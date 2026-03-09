package com.lautarorisso.blogging_platform_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lautarorisso.blogging_platform_api.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
