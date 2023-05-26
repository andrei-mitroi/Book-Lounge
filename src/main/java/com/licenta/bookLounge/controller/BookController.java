package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.BookLoungeApplication;
import com.licenta.bookLounge.exception.BookNotFoundException;
import com.licenta.bookLounge.model.BookRequest;
import com.licenta.bookLounge.model.BookResponse;
import com.licenta.bookLounge.service.BookService;
import com.licenta.bookLounge.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("BookLounge/v1")
@RequiredArgsConstructor
public class BookController {
   private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);
   private final BookService bookService;
   private final S3Service s3Service;

   @GetMapping("/getAllBooks")
   public ResponseEntity<List<BookResponse>> getAllBooks() {
      try {
         List<BookResponse> books = bookService.getAllBooks();
         if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
         }
         return ResponseEntity.ok(books);
      } catch (Exception e) {
         logger.error("Failed to retrieve books: " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @GetMapping("/getBook/{bookId}")
   public ResponseEntity<BookResponse> getBook(@PathVariable String bookId) {
      try {
         BookResponse book = bookService.getBook(bookId);
         return ResponseEntity.ok(book);
      } catch (BookNotFoundException ex) {
         throw ex;
      } catch (Exception e) {
         logger.error("Failed to retrieve book with ID " + bookId + ": " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @PostMapping("/addBook")
   public ResponseEntity<BookResponse> addBook(@RequestParam("file") MultipartFile file,
                                               @ModelAttribute BookRequest bookRequest) {
      try {
         String folderName = bookRequest.getGenre();
         String pdfLink = s3Service.uploadFile(file, folderName);
         BookResponse savedBook = bookService.addBook(bookRequest);
         bookRequest.setPdfLink(pdfLink);
         if (savedBook == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
         }
         return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
      } catch (Exception e) {
         logger.error("Failed to add book: " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @PutMapping("/updateBook/{bookId}")
   public ResponseEntity<BookResponse> updateBook(@PathVariable String bookId, @RequestBody BookRequest bookRequest) {
      try {
         BookResponse updatedBook = bookService.updateBook(bookId, bookRequest);
         if (updatedBook == null) {
            return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(updatedBook);
      } catch (Exception e) {
         logger.error("Failed to update book with ID " + bookId + ": " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @DeleteMapping("/deleteBook/{bookId}")
   public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
      try {
         boolean deleted = bookService.deleteBook(bookId);
         if (!deleted) {
            return ResponseEntity.notFound().build();
         }
         return ResponseEntity.noContent().build();
      } catch (Exception e) {
         logger.error("Failed to delete book with ID " + bookId + ": " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }
}

