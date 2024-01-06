package com.example.bookstore.app.service;


import com.example.bookstore.app.exception.AppError;
import com.example.bookstore.app.exception.DuplicateException;
import com.example.bookstore.app.exception.NoRowsUpdatedException;
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

    public ResponseEntity<?> createGenre(Genre_model genreEntity) {

        try {
            return new ResponseEntity<>(
                    genreRepository.createGenre(genreEntity),
                    HttpStatus.OK
            );
        } catch (DuplicateException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    public ResponseEntity<?> deleteGenre(Long genreId) {

        try {
            genreRepository.deleteGenre(genreId);
            return new ResponseEntity<>(
                    "Genre with id '%s' was deleted".formatted(genreId),
                    HttpStatus.OK
            );
        } catch (NoRowsUpdatedException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    public ResponseEntity<Collection<Genre_entity>> getGenres(Long offset, Long limit) {

        return new ResponseEntity<>(
                genreRepository.getGenres(offset, limit),
                HttpStatus.OK
        );
    }
}
