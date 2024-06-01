package com.wonkwang.health.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final String folderName = "products/";

    public String uploadFile(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(bucketName, folderName + fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String imageUrl = amazonS3.getUrl(bucketName, folderName + fileName).toString();
        log.info("{} 이미지 업로드 완료", imageUrl);
        return imageUrl;
    }

    public void deleteFile(String fileUrl)  {
        String fileName = extractFileNameFromUrl(fileUrl);
        amazonS3.deleteObject(bucketName, folderName + fileName);
        log.info("{} 이미지 삭제 완료", fileName);
    }

    private String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("URL이 null이거나 비어 있습니다.");
        }
        String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8);
        return decodedUrl.substring(decodedUrl.lastIndexOf('/') + 1);
    }

}
