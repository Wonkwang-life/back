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
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private String storeLink;

    @ElementCollection
    @OrderColumn(name = "image_url_order")
    private List<String> imageUrls;

    public Post(PostDTO postDTO) {
        title = postDTO.getTitle();
        content = postDTO.getContent();
        storeLink = postDTO.getStoreLink();
        imageUrls = postDTO.getImageUrls();
    }

    public void updatePost(PostDTO postDTO) {
        title = postDTO.getTitle();
        content = postDTO.getContent();
        storeLink = postDTO.getStoreLink();
        imageUrls = postDTO.getImageUrls();
    }
}
