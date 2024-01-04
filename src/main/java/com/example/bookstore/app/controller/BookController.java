package com.example.bookstore.app.controller;


import com.example.bookstore.app.enums.AppConstants;
import com.example.bookstore.app.enums.Book_sort;
import com.example.bookstore.app.model.book.Book_SummaryDto;
import com.example.bookstore.app.model.book.Book_entity;
import com.example.bookstore.app.model.book.Book_model;
import com.example.bookstore.app.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;


//ok
    @GetMapping("/books/{book_id}")
    public ResponseEntity<?> getBookById(@PathVariable Long book_id) {
        return bookService.getBookById(book_id);
    }

//ok
    @DeleteMapping("/admin/books/{book_id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long book_id) {
        return bookService.deleteBook(book_id);
    }

//ok
    @PutMapping("/admin/books")
    public ResponseEntity<?> editBook(@RequestBody Book_entity book) {
        return bookService.editBook(book);
    }

//ok
    @PostMapping("/admin/books")
    public ResponseEntity<?> createBook(@RequestBody Book_model book) {
        return bookService.createBook(book);
    }

//ok
    @GetMapping("/private/books")
    public ResponseEntity<Collection<Book_SummaryDto>> getBooksOfCustomer(
            Principal principal,
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit
    ) {
        return bookService.getBooksOfCustomer(principal, offset, limit);
    }

//ok
    @GetMapping("/books")
    public ResponseEntity<Collection<Book_SummaryDto>> getBooks(
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit,
            @RequestParam(required = false, defaultValue = AppConstants.QUERY_DEFAULT_VALUE) String query,
            @RequestParam(required = false, defaultValue = "") Book_sort sort
    ) {
        return bookService.getBooks(offset,limit,query,sort);
    }

//ok
    @PostMapping("/private/buyBook/{book_id}")
    public ResponseEntity<?> buyBook(
            Principal principal,
            @PathVariable Long book_id

    ) {
        return bookService.buyBook(principal, book_id);
    }


}
