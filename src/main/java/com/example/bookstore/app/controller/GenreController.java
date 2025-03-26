package com.example.bookstore.app.controller;

import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.model.genre.Genre_entity;
import com.example.bookstore.app.model.genre.Genre_model;
import com.example.bookstore.app.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GenreController {

    private final GenreService genreService;

    @PostMapping("/admin/genres")
    public ResponseEntity<Long> createGenre(
            @RequestBody Genre_model genreEntity
    ) {
        return genreService.createGenre(genreEntity);
    }

    @DeleteMapping("/admin/genres/{genre_id}")
    public ResponseEntity<String> deleteGenre(
            @PathVariable Long genre_id
    ) {
        return genreService.deleteGenre(genre_id);
    }

    @GetMapping("/genres")
    public ResponseEntity<Collection<Genre_entity>> getGenres(
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit
    ) {
        return genreService.getGenres(offset, limit);
    }
}
