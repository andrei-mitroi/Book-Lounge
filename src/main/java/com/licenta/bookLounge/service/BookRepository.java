package com.licenta.bookLounge.service;


import com.licenta.bookLounge.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

}
