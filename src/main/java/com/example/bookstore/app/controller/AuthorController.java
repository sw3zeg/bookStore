package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.repository.AuthorDao;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping ("/authors")
public class AuthorController {

    private final AuthorDao dao;

    @PostMapping
    public Long createAuthor(@RequestBody Author_model author) {
        return dao.createAuthor(author);
    }

    @PutMapping
    public void editAuthor(@RequestBody Author_entity author) {
        dao.editAuthor(author);
    }

    @DeleteMapping("/{author_id}")
    public void deleteAuthor(@PathVariable Long author_id) {
        dao.deleteAuthor(author_id);
    }

    @GetMapping("/{author_id}")
    public Author_entity getAuthorById(@PathVariable Long author_id) {
        return dao.getAuthorById(author_id);
    }

    @GetMapping
    public List<Author_entity> getAuthors(
            @RequestParam(required = false, defaultValue = "-1") Long offset,
            @RequestParam(required = false, defaultValue = "-1") Long limit,
            @RequestParam(required = false, defaultValue = "%") String query
    ){
        return dao.getAuthors(offset, limit, query);
    }
}
