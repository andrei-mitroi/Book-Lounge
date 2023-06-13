package com.licenta.bookLounge.service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class RepositoryService {

   private final SecretsManagerService service;

   @Value("${spring.aws.secretName}")
   private String secretName;

   private String username;

   private String password;

   private MongoDatabase database;

   @PostConstruct
   void init(){
      username = service.getSecret(secretName, "databaseUser");
      password = service.getSecret(secretName, "databasePassword");
      database = connectToMongoDB();
   }

   public MongoDatabase connectToMongoDB() {
      String mongoUri = String.format("mongodb+srv://" + username + ":" + password + "@booklounge.b754vyg.mongodb.net/?tls=true");
      ConnectionString connectionString = new ConnectionString(mongoUri);
      MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

      MongoClient mongoClient = MongoClients.create(settings);

      return mongoClient.getDatabase("BookLounge");
   }
}
