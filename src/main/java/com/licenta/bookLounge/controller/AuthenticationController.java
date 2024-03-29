package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.BookLoungeApplication;
import com.licenta.bookLounge.model.AuthenticationRequest;
import com.licenta.bookLounge.model.AuthenticationResponse;
import com.licenta.bookLounge.model.RegisterRequest;
import com.licenta.bookLounge.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("BookLounge/v1/")
@RequiredArgsConstructor
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);
	private final AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		logger.info("Registering user {}", request.getFirstName() + " " + request.getLastName());
		return ResponseEntity.ok(service.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		logger.info("Authenticating user {}", request.getEmail());
		return ResponseEntity.ok(service.authenticate(request));
	}
}