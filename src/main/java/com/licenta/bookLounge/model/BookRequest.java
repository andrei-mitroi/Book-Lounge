package com.licenta.bookLounge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
   private String title;
   private String author;
   private String genre;
   private String description;
   private String pdfLink;
}