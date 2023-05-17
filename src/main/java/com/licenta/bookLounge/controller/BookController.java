package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.BookLoungeApplication;
import com.licenta.bookLounge.exception.BookNotFound;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.repository.BookRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("BookLounge/v1")
@RequiredArgsConstructor
public class BookController {

   private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);
   private final BookRepository bookRepository;

   @GetMapping("/getAllBooks")
   public ResponseEntity<List<Book>> getAllBooks() {
      try {
         List<Book> books = bookRepository.findAll();
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
   public ResponseEntity<Book> getBook(@PathVariable String bookId) {
      try {
         Optional<Book> optionalBook = bookRepository.findById(bookId);
         if (optionalBook.isPresent()) {
            return ResponseEntity.ok(optionalBook.get());
         } else {
            throw new BookNotFound("Book with ID " + bookId + " not found");
         }
      } catch (BookNotFound ex) {
         throw ex;
      } catch (Exception e) {
         logger.error("Failed to retrieve book with ID " + bookId + ": " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @PostMapping("/addBook")
   public ResponseEntity<Book> addBook(@RequestBody Book book) {
      try {
         boolean bookExists = bookRepository.existsByTitle(book.getTitle());
         if (bookExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
         }
         logger.info("Saving " + book.getTitle() + " by " + book.getAuthor() + " to the database.");
         Book savedBook = bookRepository.save(book);
         return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
      } catch (Exception e) {
         logger.error("Failed to add book: " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @PutMapping("/updateBook/{bookId}")
   public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody Book updatedBook) {
      try {
         logger.info("Updating information for " + updatedBook.getTitle() + " by " + updatedBook.getAuthor() + ".");
         Optional<Book> optionalBook = bookRepository.findById(bookId);
         if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
         }

         updatedBook.setId(bookId);
         Book savedBook = bookRepository.save(updatedBook);
         return ResponseEntity.ok(savedBook);
      } catch (Exception e) {
         logger.error("Failed to update book with ID " + bookId + ": " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   @DeleteMapping("/deleteBook/{bookId}")
   public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
      try {
         Optional<Book> optionalBook = bookRepository.findById(bookId);
         if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
         }

         bookRepository.deleteById(bookId);
         return ResponseEntity.noContent().build();
      } catch (Exception e) {
         logger.error("Failed to delete book with ID " + bookId + ": " + e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }
}
