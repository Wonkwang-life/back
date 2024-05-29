package com.wonkwang.health.domain;

import com.wonkwang.health.dto.PostDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;

    @ElementCollection
    private List<String> fileUrls;

    public Post(PostDTO postDTO) {
        content = postDTO.getContent();
        fileUrls = postDTO.getFileUrls();
    }
}
