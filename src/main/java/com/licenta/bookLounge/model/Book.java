package com.licenta.bookLounge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Book {

    @Id
    private String id;

    private String title;
    private String author;
    private String genre;
    private String description;
    private boolean availability;

}
