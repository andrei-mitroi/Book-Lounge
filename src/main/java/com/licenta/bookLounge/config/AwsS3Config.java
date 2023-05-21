package com.licenta.bookLounge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class AwsS3Config {

    @Value("${spring.aws.profile.name}")
    private String awsProfileName;
    @Value("${spring.aws.profile.region}")
    private Region awsRegion;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(){
        return ProfileCredentialsProvider.builder().profileName(awsProfileName).build();
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider){
        return S3Client.builder().region(awsRegion).credentialsProvider(awsCredentialsProvider).build();
    }

}
