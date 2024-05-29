package com.wonkwang.health.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wonkwang.health.domain.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostDTO {

    private Long id;
    private String content;
    private List<String> fileUrls;

    @JsonCreator
    public PostDTO(@JsonProperty String content, @JsonProperty List<String> fileUrls) {
        this.id = null;
        this.content = content;
        this.fileUrls = fileUrls;
    }

    public PostDTO(Post post) {
        id = post.getId();
        content = post.getContent();
        fileUrls = post.getFileUrls();
    }
}
