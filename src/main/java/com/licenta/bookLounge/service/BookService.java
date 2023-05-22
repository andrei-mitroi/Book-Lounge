package com.licenta.bookLounge.service;

import com.licenta.bookLounge.exception.BookNotFound;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.model.BookRequest;
import com.licenta.bookLounge.model.BookResponse;
import com.licenta.bookLounge.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final S3Service s3Service;

    public BookService(BookRepository bookRepository, S3Service s3Service) {
        this.bookRepository = bookRepository;
        this.s3Service = s3Service;
    }

    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> bookResponses = new ArrayList<>();
        for (Book book : books) {
            BookResponse bookResponse = createBookResponse(book);
            bookResponses.add(bookResponse);
        }
        return bookResponses;
    }

    public BookResponse getBook(String bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String downloadLink = s3Service.generatePresignedUrl(book.getFileName());
            BookResponse bookResponse = createBookResponse(book);
            bookResponse.setDownloadLink(downloadLink);
            return bookResponse;
        } else {
            throw new BookNotFound("Book with ID " + bookId + " not found");
        }
    }

    public BookResponse addBook(BookRequest bookRequest) {
        if (bookExistsByTitle(bookRequest.getTitle())) {
            return null;
        }
        Book book = createBook(bookRequest);
        Book savedBook = bookRepository.save(book);
        return createBookResponse(savedBook);
    }

    public BookResponse updateBook(String bookId, BookRequest bookRequest) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return null;
        }
        Book book = optionalBook.get();
        updateBookFromRequest(book, bookRequest);
        Book savedBook = bookRepository.save(book);
        return createBookResponse(savedBook);
    }
    public boolean deleteBook(String bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return false;
        }
        bookRepository.deleteById(bookId);
        return true;
    }

    private BookResponse createBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setGenre(book.getGenre());
        bookResponse.setDescription(book.getDescription());
        bookResponse.setFileName(book.getFileName());
        return bookResponse;
    }

    private boolean bookExistsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    private Book createBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setGenre(bookRequest.getGenre());
        book.setDescription(bookRequest.getDescription());
        book.setFileName(bookRequest.getFileName());
        return book;
    }

    private void updateBookFromRequest(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setGenre(bookRequest.getGenre());
        book.setDescription(bookRequest.getDescription());
        book.setFileName(bookRequest.getFileName());
    }
}
