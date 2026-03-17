package com.lautarorisso.blogging_platform_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lautarorisso.blogging_platform_api.model.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("""
            SELECT p FROM Post p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
            OR LOWER(p.content) LIKE LOWER(CONCAT('%', :term, '%'))
            OR LOWER(p.category) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    List<PostEntity> search(String term);
}
