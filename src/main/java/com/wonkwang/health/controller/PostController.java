package com.wonkwang.health.controller;

import com.wonkwang.health.dto.PostDTO;
import com.wonkwang.health.dto.ResponseDTO;
import com.wonkwang.health.dto.ResponseEntityBuilder;
import com.wonkwang.health.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDTO<PostDTO>> getPost(@PathVariable Long postId) {
         
        return ResponseEntityBuilder.build("글 불러오기 완료", OK, postService.getOnePost(postId));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> createPost(@RequestBody PostDTO postDTO) {
        System.out.println("postDTO = " + postDTO);
        postService.savePost(postDTO);
        return ResponseEntityBuilder.build("글 생성 완료", OK);
    }
}
