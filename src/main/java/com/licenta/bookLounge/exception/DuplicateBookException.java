package com.licenta.bookLounge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateBookException extends RuntimeException {
	public DuplicateBookException(String message) {
		super(message);
	}
}