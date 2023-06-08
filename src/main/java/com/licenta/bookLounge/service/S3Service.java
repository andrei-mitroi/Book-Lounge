package com.licenta.bookLounge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	public void uploadFile(MultipartFile file, String folderName, String bucketName) throws IOException {
		String fileName = file.getOriginalFilename();
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
		String key = folderName + "/" + fileName;

		Path tempFile = Files.createTempFile("temp-", encodedFileName);
		file.transferTo(tempFile.toFile());

		s3Client.putObject(PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build(), tempFile);

		Files.delete(tempFile);
	}
}
