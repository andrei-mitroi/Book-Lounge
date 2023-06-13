package com.licenta.bookLounge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
public class SecretsManagerConfig {

   @Value("${spring.aws.region}")
   private String region;

   @Value("${spring.aws.accessKey}")
   private String awsAccessKey;

   @Value("${spring.aws.secretKey}")
   private String awsSecretKey;

   @Bean
   public SecretsManagerClient secretsManagerClient() {

      AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
              AwsBasicCredentials.create(awsAccessKey, awsSecretKey));

      return SecretsManagerClient.builder()
              .region(Region.of(region))
              .credentialsProvider(credentialsProvider)
              .build();
   }
}
