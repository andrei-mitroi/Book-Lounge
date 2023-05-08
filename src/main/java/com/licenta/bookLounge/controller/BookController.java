package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.exception.BookNotFound;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.model.BookRequest;
import com.licenta.bookLounge.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class BookController {

   private final BookRepository bookRepository;

   @GetMapping("/books")
   public List<Book> getAllBooks() {
      return bookRepository.findAll();
   }

   @GetMapping("/books/{id}")
   public Book getBook(@PathVariable String id) {
      return bookRepository.findById(id).orElseThrow(() -> new BookNotFound(id));
   }
   @PostMapping("/books/")
   public ResponseEntity<Void> createBook(@RequestBody BookRequest bookRequest) {
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
   public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
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
