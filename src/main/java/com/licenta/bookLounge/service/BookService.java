package com.licenta.bookLounge.service;

import com.licenta.bookLounge.exception.BookNotFoundException;
import com.licenta.bookLounge.exception.DuplicateBookException;
import com.licenta.bookLounge.model.Book;
import com.licenta.bookLounge.model.BookRequest;
import com.licenta.bookLounge.model.BookResponse;
import com.licenta.bookLounge.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public PaginatedBookResponse getAllBooks(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("author").ascending());
		Page<Book> bookPage = bookRepository.findAll(pageable);
		List<BookResponse> bookResponses = bookPage.stream()
				.map(this::createBookResponse)
				.collect(Collectors.toList());
		return new PaginatedBookResponse(bookResponses, bookPage.getTotalElements());
	}


	public BookResponse getBook(String bookId) {
		Book book = findBookById(bookId);
		return createBookResponse(book);
	}

	public BookResponse addBook(BookRequest bookRequest) {
		validateBookNotExistsByTitle(bookRequest.getTitle());
		Book book = createBook(bookRequest);
		Book savedBook = bookRepository.save(book);
		return createBookResponse(savedBook);
	}

	private Book findBookById(String bookId) {
		return bookRepository.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
	}

	private void validateBookNotExistsByTitle(String title) {
		if (bookRepository.existsByTitle(title)) {
			throw new DuplicateBookException("Book with title " + title + " already exists");
		}
	}

	private BookResponse createBookResponse(Book book) {
		return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(),
				book.getDescription(), book.getPdfLink());
	}

	private Book createBook(BookRequest bookRequest) {
		return Book.builder()
				.title(bookRequest.getTitle())
				.author(bookRequest.getAuthor())
				.genre(bookRequest.getGenre())
				.description(bookRequest.getDescription())
				.pdfLink(bookRequest.getPdfLink())
				.build();
	}
}
