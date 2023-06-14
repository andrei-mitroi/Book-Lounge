package com.licenta.bookLounge.service;

import com.licenta.bookLounge.BookLoungeApplication;
import com.licenta.bookLounge.exception.DuplicateEmail;
import com.licenta.bookLounge.model.*;
import com.licenta.bookLounge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);
	private final UserRepository userRepository;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EmailSender emailSender;

	public AuthenticationResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			logger.error(
					"Could not create user because email address " + request.getEmail() + " already exists in database.");
			throw new DuplicateEmail("Email already exists.");
		}
		var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER).hasUploadedBook(false)
				.build();
		userService.saveUser(user);
		var jwtToken = jwtService.generateToken(user);
		emailSender.send(request.getEmail(), buildEmail(request));
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		boolean hasUploadedBook = user.isHasUploadedBook();
		return AuthenticationResponse.builder().token(jwtToken).hasUploadedBook(hasUploadedBook).build();
	}

	private String buildEmail(RegisterRequest request) {
		String htmlTemplate = "";
		try {
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource("static/email-template.html");
			htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()));
			htmlTemplate = htmlTemplate.replace("[User's Name]", request.getFirstName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return htmlTemplate;
	}
}