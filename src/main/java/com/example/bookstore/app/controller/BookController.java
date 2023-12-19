package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.book.Book_entity;
import com.example.bookstore.app.model.book.Book_model;
import com.example.bookstore.app.model.book.Book_sort;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.repository.BookDao;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookDao dao;

    @PostMapping
    public Long createBook(@RequestBody Book_model book) {
        return dao.createBook(book);
    }

    @PutMapping
    public void editBook(@RequestBody Book_entity book) {
        dao.editBook(book);
    }

    @DeleteMapping("/{book_id}")
    public void deleteBook(@PathVariable Long book_id) {
        dao.deleteBook(book_id);
    }

    @GetMapping("/{book_id}")
    public Book_view getBookById(@PathVariable Long book_id) {
        return dao.getBookById(book_id);
    }

    @GetMapping
    public List<Book_view> getBooks(
            @RequestParam(required = false, defaultValue = "-1") Long offset,
            @RequestParam(required = false, defaultValue = "-1") Long limit,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") Book_sort sort
            ){
        return dao.getBooks(offset,limit,sort,query);
    }

}
