package com.licenta.bookLounge.service;

import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {
   private final BookRepository bookRepository;
   private static final Logger logger = LoggerFactory.getLogger(BookService.class);

   public Book saveBook(Book book) {
      logger.info("Saving book: {}", book);
      return bookRepository.save(book);
   }
}
