package com.wonkwang.health.controller;

import com.wonkwang.health.dto.PostDTO;
import com.wonkwang.health.dto.ResponseDTO;
import com.wonkwang.health.dto.ResponseEntityBuilder;
import com.wonkwang.health.service.PostService;
import com.wonkwang.health.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

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

    @PatchMapping("/{postId}")
    public ResponseEntity<ResponseDTO<?>> updatePost(@RequestBody PostDTO postDTO, @PathVariable Long postId) {
        System.out.println("postDTO = " + postDTO);
        postService.updatePost(postId, postDTO);
        return ResponseEntityBuilder.build("글 수정 완료", OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDTO<?>> deletePost(@PathVariable Long postId) {
        postService.deleteOnePost(postId);
        return ResponseEntityBuilder.build("글 삭제 완료", OK);
    }

    @PostMapping("/image")
    public List<String> uploadImages(@RequestParam("file") MultipartFile[] files) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = s3Service.uploadFile(file);
            imageUrls.add(url);
        }
        System.out.println("Uploaded files: " + imageUrls.size()); // 업로드된 파일 수 출력
        return imageUrls;
    }
}
