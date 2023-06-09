package com.licenta.bookLounge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

	@Value("${spring.aws.region}")
	private String awsRegion;

	@Value("${spring.aws.accessKey}")
	private String awsAccessKeyId;

	@Value("${spring.aws.secretKey}")
	private String awsSecretKey;

	@Bean
	public S3Client s3Client() {
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
				AwsBasicCredentials.create(awsAccessKeyId, awsSecretKey));

		return S3Client.builder()
				.region(Region.of(awsRegion))
				.credentialsProvider(credentialsProvider)
				.build();
	}
}