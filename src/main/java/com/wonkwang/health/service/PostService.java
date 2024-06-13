package com.wonkwang.health.service;

import com.wonkwang.health.domain.Post;
import com.wonkwang.health.dto.PostDTO;
import com.wonkwang.health.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final DiscordService discordService;

    public Long createPost(PostDTO postDTO) {
        String sanitizedContent = sanitize(postDTO.getContent()); //악성 스크립트 제거
        postDTO.setContent(sanitizedContent);
        Post post = new Post(postDTO);
        Post savedPost = postRepository.save(post);

        discordService.sendActivityMessage(postDTO.toString() + " : 제품을 등록했습니다.");
        log.info("{} : 제품을 등록했습니다.", postDTO.toString());

        return savedPost.getId();
    }
    @Transactional
    public void updatePost(Long postId, PostDTO postDTO) {
        Post findPost = postRepository.findPostWithUrlsById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 글이 없습니다."));

        String sanitizedContent = sanitize(postDTO.getContent()); //악성 스크립트 제거
        postDTO.setContent(sanitizedContent);

        // 기존 이미지 URL 리스트
        List<String> existingImageUrls = findPost.getImageUrls();

        // 새로운 이미지 URL 리스트
        List<String> newImageUrls = postDTO.getImageUrls();

        // 삭제된 이미지 URL 리스트
        List<String> deletedImageUrls = existingImageUrls.stream()
                .filter(url -> !newImageUrls.contains(url))
                .toList();

        // S3에서 삭제 요청
        deletedImageUrls.forEach(s3Service::deleteFile);

        findPost.updatePost(postDTO);

        discordService.sendActivityMessage(postDTO.toString() + " : 제품을 수정했습니다.");
        log.info("{} 글 수정 완료", postId);
    }

    @Transactional
    public PostDTO getOnePost(Long postId) {
        Post findPost = postRepository.findPostWithUrlsById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 글이 없습니다."));
        findPost.addHit();

        return new PostDTO(findPost);
    }

    public List<PostDTO> searchPost(String keyword) {
        List<Post> searchList = postRepository.findByTitleContaining(keyword);
        return searchList.stream().map(PostDTO::new).toList();
    }

    public Page<PostDTO> getPostList(Pageable pageable) {
        Page<Post> findPosts = postRepository.findAllWithImageUrls(pageable);
        return findPosts.map(PostDTO::new);
    }

    public void deleteOnePost(Long postId) {
        Post findPost = postRepository.findPostWithUrlsById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 글이 없습니다."));

        List<String> imageUrls = findPost.getImageUrls();
        imageUrls.forEach(s3Service::deleteFile);

        postRepository.delete(findPost);

        discordService.sendActivityMessage(postId + " : 제품을 삭제했습니다.");
        log.info("{} 글 삭제 완료", postId);
    }

    private String sanitize(String content) {
        // 정규 표현식을 사용하여 여러 태그와 속성을 제거
        String cleanContent = content.replaceAll("(?i)<script.*?>.*?</script.*?>", ""); // script 태그 제거
        cleanContent = cleanContent.replaceAll("(?i)<img.*?>", "[removed]"); // img 태그 제거
        cleanContent = cleanContent.replaceAll("(?i)\\s+on\\w+\\s*=\\s*\"[^\"]*\"", ""); // 이벤트 핸들러 속성 제거
        cleanContent = cleanContent.replaceAll("(?i)\\s+on\\w+\\s*=\\s*'[^']*'", ""); // 이벤트 핸들러 속성 제거 (단일 인용부호)
        cleanContent = cleanContent.replaceAll("(?i)\\s+javascript\\s*:\\s*[^\"]*\"", ""); // javascript: 속성 제거
        cleanContent = cleanContent.replaceAll("(?i)\\s+javascript\\s*:\\s*[^']*'", ""); // javascript: 속성 제거 (단일 인용부호)
        return cleanContent;
    }

}

