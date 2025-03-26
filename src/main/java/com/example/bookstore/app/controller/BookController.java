package com.example.bookstore.app.controller;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.enums.Book_sort;
import com.example.bookstore.app.model.book.Book_SummaryDto;
import com.example.bookstore.app.model.book.Book_entity;
import com.example.bookstore.app.model.book.Book_model;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @GetMapping("/books/{book_id}")
    public ResponseEntity<Book_view> getBookById(@PathVariable Long book_id) {
        return bookService.getBookById(book_id);
    }

    @DeleteMapping("/admin/books/{book_id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long book_id) {
        return bookService.deleteBook(book_id);
    }

    @PutMapping("/admin/books")
    public ResponseEntity<String> editBook(@RequestBody Book_entity book) {
        return bookService.editBook(book);
    }

    @PostMapping("/admin/books")
    public ResponseEntity<Long> createBook(@RequestBody Book_model book) {
        return bookService.createBook(book);
    }

    @GetMapping("/private/books")
    public ResponseEntity<Collection<Book_SummaryDto>> getBooksOfCustomer(
            Principal principal,
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit
    ) {
        return bookService.getBooksOfCustomer(principal, offset, limit);
    }

    @GetMapping("/books")
    public ResponseEntity<Collection<Book_SummaryDto>> getBooks(
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit,
            @RequestParam(required = false, defaultValue = AppConstants.QUERY_DEFAULT_VALUE) String query,
            @RequestParam(required = false, defaultValue = "") Book_sort sort
    ) {
        return bookService.getBooks(offset,limit,query,sort);
    }

    @PostMapping("/private/buyBook/{book_id}")
    public ResponseEntity<String> buyBook(
            Principal principal,
            @PathVariable Long book_id

    ) {
        return bookService.buyBook(principal, book_id);
    }


}
