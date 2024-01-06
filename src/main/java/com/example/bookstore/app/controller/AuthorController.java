package com.example.bookstore.app.controller;


import com.example.bookstore.app.enums.AppConstants;
import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.repository.AuthorDao;
import com.example.bookstore.app.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping ("/api")
public class AuthorController {

    private final AuthorService authorService;

//ok
    @PostMapping("/admin/authors")
    public ResponseEntity<?> createAuthor(@RequestBody Author_model author) {
        return authorService.createAuthor(author);
    }

//ok
    @PutMapping("/admin/authors")
    public ResponseEntity<?> editAuthor(@RequestBody Author_entity author) {
        return authorService.editAuthor(author);
    }

//ok
    @DeleteMapping("/admin/authors/{author_id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long author_id) {
        return authorService.deleteAuthor(author_id);
    }

//ok
    @GetMapping("/authors/{author_id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long author_id) {
        return authorService.getAuthorById(author_id);
    }

//ok
    @GetMapping("/authors")
    public ResponseEntity<Collection<Author_entity>> getAuthors(
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit,
            @RequestParam(required = false, defaultValue = AppConstants.QUERY_DEFAULT_VALUE) String query
    ){
        return authorService.getAuthors(offset, limit, query);
    }
}
