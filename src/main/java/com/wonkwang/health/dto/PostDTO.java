package com.wonkwang.health.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wonkwang.health.domain.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private String storeLink;
    private List<String> imageUrls;

    @JsonCreator
    public PostDTO(@JsonProperty String title,
                   @JsonProperty String content,
                   @JsonProperty String storeLink,
                   @JsonProperty List<String> imageUrls) {
        this.id = null;
        this.title = title;
        this.content = content;
        this.storeLink = storeLink;
        this.imageUrls = imageUrls;
    }

    public PostDTO(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        storeLink = post.getStoreLink();
        imageUrls = post.getImageUrls();
    }
}
