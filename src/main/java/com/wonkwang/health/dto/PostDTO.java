package com.wonkwang.health.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wonkwang.health.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private Long hit;
    private String title;
    private String oneLineIntroduce;
    private String productType;
    private String packingUnit;
    private String tag;
    private String content;
    private String storeLink;
    private List<String> imageUrls;


    public PostDTO(Post post) {
        id = post.getId();
        hit = post.getHit();
        title = post.getTitle();
        content = post.getContent();
        storeLink = post.getStoreLink();
        imageUrls = post.getImageUrls();
        oneLineIntroduce = post.getOneLineIntroduce();
        productType = post.getProductType();
        packingUnit = post.getPackingUnit();
        tag = post.getTag();
    }
}
