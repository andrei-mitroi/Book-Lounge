package com.licenta.bookLounge.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Books")
public class Book {

    @Id
    private String id = UUID.randomUUID().toString();
    private String title;
    private String author;
    private String genre;
    private String description;

}
