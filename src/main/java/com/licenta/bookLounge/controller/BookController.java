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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("BookLounge/v1")
@RequiredArgsConstructor
public class BookController {

   private static final Logger logger = LoggerFactory.getLogger(BookLoungeApplication.class);
   private final BookService bookService;
   private final S3Service s3Service;

   @Value("${spring.aws.bucketName}")
   private String bucketName;
   @Value("${spring.aws.region}")
   String region;


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
   public ResponseEntity<Resource> getBook(@PathVariable String bookId) {
      try {
         BookResponse book = bookService.getBook(bookId);
         String pdfLink = book.getPdfLink();

         S3Client s3Client = S3Client.builder()
                 .region(Region.of(region))
                 .build();

         String bucketName = extractBucketNameFromS3Uri(pdfLink);
         String objectKey = extractObjectKeyFromS3Uri(pdfLink);

         GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                 .bucket(bucketName)
                 .key(objectKey)
                 .build();

         ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);

         byte[] fileBytes = responseBytes.asByteArray();
         Resource resource = new ByteArrayResource(fileBytes);

         HttpHeaders headers = new HttpHeaders();
         headers.setContentDisposition(ContentDisposition.attachment().filename(book.getTitle() + ".pdf").build());

         return ResponseEntity.ok()
                 .headers(headers)
                 .contentLength(fileBytes.length)
                 .contentType(MediaType.APPLICATION_PDF)
                 .body(resource);
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
         s3Service.uploadFile(file, folderName, bucketName);

         String pdfLink= createPdfLink(bookRequest);
         bookRequest.setPdfLink(pdfLink);
         BookResponse savedBook = bookService.addBook(bookRequest);
         if (savedBook == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

   private String extractBucketNameFromS3Uri(String uri) {
      int startIndex = uri.indexOf("//") + 2;
      int endIndex = uri.indexOf("/", startIndex);
      return uri.substring(startIndex, endIndex);
   }

   private String extractObjectKeyFromS3Uri(String uri) {
      int startIndex = uri.indexOf("/", uri.indexOf("//") + 2) + 1;
      return uri.substring(startIndex);
   }

   private String createPdfLink(BookRequest bookRequest){
      return "s3://" + bucketName + "/" + bookRequest.getGenre() + "/" + bookRequest.getTitle() + ".pdf";
   }
}

