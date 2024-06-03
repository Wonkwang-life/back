package com.wonkwang.health.repository;

import com.wonkwang.health.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = "imageUrls")
    Optional<Post> findPostWithUrlsById(Long id);
}
