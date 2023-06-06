package com.licenta.bookLounge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactForm {
    private String name;
    private String email;
    private String message;
}
