package com.wonkwang.health.controller;

import com.wonkwang.health.domain.Role;
import com.wonkwang.health.dto.PostDTO;
import com.wonkwang.health.dto.ResponseDTO;
import com.wonkwang.health.dto.ResponseEntityBuilder;
import com.wonkwang.health.service.PostService;
import com.wonkwang.health.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wonkwang.health.dto.ResponseEntityBuilder.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDTO<PostDTO>> getPost(@PathVariable Long postId) {

        return build("글 불러오기 완료", OK, postService.getOnePost(postId));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<PostDTO>>> searchPost(@RequestParam String keyword) {
        if (keyword.length() < 2) {
            return build("최소 2글자 이상 입력해주세요.", BAD_REQUEST, null);
        }
        return build(keyword + " : 검색 완료", OK, postService.searchPost(keyword));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<PostDTO>>> getPostList(@PageableDefault(size=30, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return build("글목록 불러오기 완료", OK, postService.getPostList(pageable));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Long>> createPost(@RequestBody PostDTO postDTO, @SessionAttribute Role role) {
        if (role != Role.ADMIN) {
            return build("권한 부족", FORBIDDEN, null);
        }
        Long createdPostId = postService.createPost(postDTO);
        return build("글 생성 완료", OK, createdPostId);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ResponseDTO<Long>> updatePost(@RequestBody PostDTO postDTO, @PathVariable Long postId, @SessionAttribute Role role) {
        if (role != Role.ADMIN) {
            return build("권한 부족", FORBIDDEN, null);
        }
        System.out.println("postDTO = " + postDTO);
        postService.updatePost(postId, postDTO);
        return build("글 수정 완료", OK, postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDTO<Long>> deletePost(@PathVariable Long postId, @SessionAttribute Role role) {
        if (role != Role.ADMIN) {
            return build("권한 부족", FORBIDDEN, null);
        }
        postService.deleteOnePost(postId);
        return build("글 삭제 완료", OK, postId);
    }

    @PostMapping("/image")
    public ResponseEntity<ResponseDTO<List<String>>> uploadImages(@RequestParam("file") MultipartFile[] files, @SessionAttribute Role role) throws IOException {
        if (role != Role.ADMIN) {
            return build("권한 부족", FORBIDDEN, null);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = s3Service.uploadFile(file);
            imageUrls.add(url);
        }
        System.out.println("Uploaded files: " + imageUrls.size()); // 업로드된 파일 수 출력
        return build("이미지 업로드 성공",OK, imageUrls);
    }
}
