package com.licenta.bookLounge.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
@RequiredArgsConstructor
public class SecretsManagerService {

   private final SecretsManagerClient secretsManagerClient;

   private final ObjectMapper objectMapper = new ObjectMapper();

   public String getSecret(String secretName, String secretKey) {
      GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
            .secretId(secretName)
            .build();

      GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(secretValueRequest);
      String secretString = secretValueResponse.secretString();

      try {
         JsonNode jsonNode = objectMapper.readTree(secretString);
         return jsonNode.get(secretKey).asText();
      } catch (Exception e) {
         throw new RuntimeException("Error retrieving secret from AWS Secret Manager.", e);
      }
   }
}
