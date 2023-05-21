package com.licenta.bookLounge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class FileDownloadS3 {

    private final S3Client s3Client;

    public void downloadFileFromS3Bucket(String bucketName, String key, String destinationPath) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest)) {
            byte[] fileBytes = responseInputStream.readAllBytes();

            try (OutputStream fileOutputStream = new FileOutputStream(destinationPath)) {
                fileOutputStream.write(fileBytes);
                System.out.println("File downloaded successfully!");
            } catch (IOException e) {
                System.err.println("Error writing file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error reading file content: " + e.getMessage());
        }
    }
}
