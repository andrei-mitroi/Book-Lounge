package com.licenta.bookLounge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@Service
public class S3Service {

    @Value("${spring.aws.region}")
    private String region;
    @Value("${spring.aws.bucketName}")
    private String bucketName;

    public String generatePresignedUrl(String fileName) {

        S3ClientBuilder clientBuilder = S3Client.builder()
                .region(Region.of(region));

        AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
        clientBuilder.credentialsProvider(credentialsProvider);

        S3Client s3Client = clientBuilder.build();

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }
}