package com.example.bookstore.app.service;


import com.example.bookstore.app.model.genre.Genre_entity;
import com.example.bookstore.app.model.genre.Genre_model;
import com.example.bookstore.app.repository.GenreDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreDao genreRepository;

    public ResponseEntity<Long> createGenre(Genre_model genreEntity) {

        return new ResponseEntity<>(
                genreRepository.createGenre(genreEntity),
                HttpStatus.OK
        );
    }


    public ResponseEntity<String> deleteGenre(Long genreId) {

        genreRepository.deleteGenre(genreId);
        return new ResponseEntity<>(
                "Genre with id '%s' was deleted".formatted(genreId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Collection<Genre_entity>> getGenres(Long offset, Long limit) {

        return new ResponseEntity<>(
                genreRepository.getGenres(offset, limit),
                HttpStatus.OK
        );
    }
}
