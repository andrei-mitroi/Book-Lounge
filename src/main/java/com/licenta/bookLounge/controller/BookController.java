package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.BookLoungeApplication;
import com.licenta.bookLounge.exception.BookNotFoundException;
import com.licenta.bookLounge.model.BookRequest;
import com.licenta.bookLounge.model.BookResponse;
import com.licenta.bookLounge.model.User;
import com.licenta.bookLounge.repository.UserRepository;
import com.licenta.bookLounge.service.BookService;
import com.licenta.bookLounge.service.PaginatedBookResponse;
import com.licenta.bookLounge.service.S3Service;
import com.licenta.bookLounge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("BookLounge/v1")
@RequiredArgsConstructor
public class BookController {

	private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);
	private final BookService bookService;
	private final S3Service s3Service;
	private final UserService userService;
	private final UserRepository userRepository;

	@Value("${spring.aws.region}")
	String region;
	@Value("${spring.aws.bucketName}")
	private String bucketName;

	@GetMapping("/getAllBooks")
	public ResponseEntity<PaginatedBookResponse> getAllBooks(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize
	) {
		try {
			PaginatedBookResponse bookResponse = bookService.getAllBooks(page, pageSize);
			if (bookResponse.getContent().isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(bookResponse);
		} catch (Exception e) {
			logger.error("Failed to retrieve books: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/getBook/{bookId}")
	public ResponseEntity<Resource> getBook(@PathVariable String bookId, Principal principal) {
		try {
			User user = userService.getUserByEmail(principal.getName());
			if (!user.isHasUploadedBook()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

			BookResponse book = bookService.getBook(bookId);
			String pdfLink = book.getPdfLink();

			byte[] fileBytes = s3Service.downloadFile(pdfLink);

			Resource resource = new ByteArrayResource(fileBytes);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentDisposition(ContentDisposition.attachment().filename(book.getTitle() + ".pdf").build());

			user.setHasUploadedBook(false);
			userRepository.save(user);

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(fileBytes.length)
					.contentType(MediaType.APPLICATION_PDF)
					.body(resource);
		} catch (BookNotFoundException ex) {
			throw ex;
		} catch (Exception e) {
			logger.error("Failed to retrieve book with ID " + bookId + ": " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/addBook")
	public ResponseEntity<BookResponse> addBook(@RequestParam("file") MultipartFile file,
			@ModelAttribute BookRequest bookRequest, Principal principal) {
		try {
			String folderName = bookRequest.getGenre();
			s3Service.uploadFile(file, folderName, bucketName);

			String pdfLink = createPdfLink(bookRequest);
			bookRequest.setPdfLink(pdfLink);
			BookResponse savedBook = bookService.addBook(bookRequest);

			User user = userService.getUserByEmail(principal.getName());

			user.setHasUploadedBook(true);
			userService.saveUser(user);

			if (savedBook == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
		} catch (Exception e) {
			logger.error("Failed to add book: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private String createPdfLink(BookRequest bookRequest) {
		return "s3://" + bucketName + "/" + bookRequest.getGenre() + "/" + bookRequest.getTitle() + ".pdf";
	}
}

