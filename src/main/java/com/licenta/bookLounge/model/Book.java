package com.licenta.bookLounge.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "Books")
public class Book {

    @Id
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private String genre;
    @NonNull
    private String description;

    private String pdfLink;

}
