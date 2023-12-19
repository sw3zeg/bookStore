package com.example.bookstore.app.controller;

import com.example.bookstore.app.model.genre.Genre_entity;
import com.example.bookstore.app.model.genre.Genre_model;
import com.example.bookstore.app.repository.GenreDao;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreDao dao;

    @PostMapping
    public Long createGenre(
            @RequestBody Genre_model genreEntity
    ) {
        return dao.createGenre(genreEntity);
    }

    @DeleteMapping("/{genre_id}")
    public void deleteGenre(
            @PathVariable Long genre_id
    ) {
        dao.deleteGenre(genre_id);
    }

    @GetMapping
    public List<Genre_entity> getGenres(
            @RequestParam(required = false, defaultValue = "-1") Long offset,
            @RequestParam(required = false, defaultValue = "-1") Long limit
    ) {
        return dao.getGenres(offset, limit);
    }
}
