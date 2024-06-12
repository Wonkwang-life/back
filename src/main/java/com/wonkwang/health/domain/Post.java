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
    private Long hit = 0L; //조회수

    private String title;
    private String oneLineIntroduce;
    private String productType;
    private String packingUnit;
    private String howEat;
    private String tag;

    @Lob
    private String content;

    private String storeLink;

    @ElementCollection
    @OrderColumn(name = "image_url_order")
    private List<String> imageUrls;

    public Post(PostDTO postDTO) {
        createEntity(postDTO);
    }
    public void updatePost(PostDTO postDTO) {
        createEntity(postDTO);
    }

    private void createEntity(PostDTO postDTO) {
        title = postDTO.getTitle();
        content = postDTO.getContent();
        storeLink = postDTO.getStoreLink();
        imageUrls = postDTO.getImageUrls();
        oneLineIntroduce = postDTO.getOneLineIntroduce();
        productType = postDTO.getProductType();
        packingUnit = postDTO.getPackingUnit();
        howEat = postDTO.getHowEat();
        tag = postDTO.getTag();
    }

    public void addHit() {
        hit += 1;
    }

}
