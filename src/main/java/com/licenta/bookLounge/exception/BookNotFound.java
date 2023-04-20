package com.licenta.bookLounge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFound extends RuntimeException{
    public BookNotFound(String id) {
        super("Book not found: " + id);
    }
}
