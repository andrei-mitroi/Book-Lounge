package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.BookLoungeApplication;
import com.licenta.bookLounge.exception.BookNotFound;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.model.BookRequest;
import com.licenta.bookLounge.model.BookResponse;
import com.licenta.bookLounge.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
   private final BookRepository bookRepository;
   private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);

   @GetMapping("/books")
   public List<Book> getAllBooks() {
      logger.info("Getting all books");
      return bookRepository.findAll();
   }

   @GetMapping("/books/{id}")
   public Book getBook(@PathVariable String id) {
      return bookRepository.findById(id).orElseThrow(() -> new BookNotFound(id));
   }
   @PostMapping("/addBook")
   public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {
      logger.info("Creating book registry for: {}", bookRequest.getTitle());
      Book book = Book.builder()
            .title(bookRequest.getTitle())
            .author(bookRequest.getAuthor())
            .genre(bookRequest.getGenre())
            .description(bookRequest.getDescription())
            .build();
      bookRepository.save(book);
      return ResponseEntity.status(HttpStatus.CREATED).build();
   }
   @PutMapping("/books/{id}")
   public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody BookRequest updatedBook) {
      logger.info("Updating book No.: {}", id);
      Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFound(id));
      book.setTitle(updatedBook.getTitle());
      book.setAuthor(updatedBook.getAuthor());
      book.setGenre(updatedBook.getGenre());
      book.setDescription(updatedBook.getDescription());
      bookRepository.save(book);
      return ResponseEntity.ok(book);
   }
   @DeleteMapping("/books/{id}")
   public ResponseEntity<Void> deleteBook(@PathVariable String id) {
      bookRepository.deleteById(id);
      return ResponseEntity.ok().build();
   }
}
