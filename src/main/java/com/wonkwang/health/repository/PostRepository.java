package com.wonkwang.health.repository;

import com.wonkwang.health.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
