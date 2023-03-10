package com.licenta.bookLounge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Book {

    @Id
    private String id;

    private String title;
    private String author;
    private String genre;
    private String description;
    private boolean availability;

}
