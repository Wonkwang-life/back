package com.wonkwang.health.service;

import com.wonkwang.health.domain.Post;
import com.wonkwang.health.dto.PostDTO;
import com.wonkwang.health.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post savePost(PostDTO postDTO) {
        Post post = new Post(postDTO);
        return postRepository.save(post);
    }

    public PostDTO getOnePost(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 글이 없습니다."));

        return new PostDTO(findPost);
    }
}
