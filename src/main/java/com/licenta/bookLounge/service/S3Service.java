package com.licenta.bookLounge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    @Value("${spring.aws.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        String fileName = file.getOriginalFilename();
        String key = folderName + "/" + fileName;

        Path tempFile = Files.createTempFile("temp-", fileName);
        file.transferTo(tempFile.toFile());

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), tempFile);

        Files.delete(tempFile);

        return key;
    }

    public InputStream downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Download the file from S3 bucket
        return s3Client.getObject(getObjectRequest);
    }

}
