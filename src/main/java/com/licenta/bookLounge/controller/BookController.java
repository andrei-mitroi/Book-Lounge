package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.exception.BookNotFound;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> createBook(@RequestBody Book book) {
        bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(String id, @RequestBody Book updatedBook){
         if (!bookRepository.existsById(id)) {
             throw new BookNotFound(id);
         }
         updatedBook.setId(id);
         return ResponseEntity.ok(bookRepository.save(updatedBook));
        }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
