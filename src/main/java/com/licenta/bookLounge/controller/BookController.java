package com.licenta.bookLounge.controller;

import com.licenta.bookLounge.exception.BookNotFound;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.service.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable String id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFound(id));
    }

    @PostMapping("/books/")
    public void saveBook(@RequestBody Book book) {
        bookRepository.save(book);
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        if (!bookExists(id)) {
            throw new BookNotFound(id);
        }
        return bookRepository.save(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(id);
    }

    private boolean bookExists(String id) {
        return bookRepository.existsById(id);
    }
}
