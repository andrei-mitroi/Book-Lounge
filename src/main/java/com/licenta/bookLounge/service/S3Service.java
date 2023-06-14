package com.licenta.bookLounge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
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

	public byte[] downloadFile(String pdfLink) {
		String bucketName = extractBucketNameFromS3Uri(pdfLink);
		String objectKey = extractObjectKeyFromS3Uri(pdfLink);

		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.build();

		ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);

		return responseBytes.asByteArray();
	}

	private String extractBucketNameFromS3Uri(String uri) {
		int startIndex = uri.indexOf("//") + 2;
		int endIndex = uri.indexOf("/", startIndex);
		return uri.substring(startIndex, endIndex);
	}

	private String extractObjectKeyFromS3Uri(String uri) {
		int startIndex = uri.indexOf("/", uri.indexOf("//") + 2) + 1;
		return uri.substring(startIndex);
	}
}
