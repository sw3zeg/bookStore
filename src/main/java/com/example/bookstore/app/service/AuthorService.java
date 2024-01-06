package com.example.bookstore.app.service;

import com.example.bookstore.app.exception.AppError;
import com.example.bookstore.app.exception.NoRowsUpdatedException;
import com.example.bookstore.app.exception.ObjectNotFoundException;
import com.example.bookstore.app.exception.TooLargeFieldException;
import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.repository.AuthorDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorDao authorRepository;

    public ResponseEntity<?> createAuthor(Author_model author) {

        try {
            return new ResponseEntity<>(
                    authorRepository.createAuthor(author),
                    HttpStatus.OK
            );
        } catch (TooLargeFieldException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public ResponseEntity<?> editAuthor(Author_entity author) {

        try {
            authorRepository.editAuthor(author);

            return new ResponseEntity<>(
                    "Author '%s' was updated".formatted(author.getFio()),
                    HttpStatus.OK
            );
        } catch (TooLargeFieldException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public ResponseEntity<?> deleteAuthor(Long author_id) {

        try {
            authorRepository.deleteAuthor(author_id);
            return new ResponseEntity<>(
                    "Author with id '%s' was deleted".formatted(author_id),
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

    public ResponseEntity<?> getAuthorById(Long author_id) {

        try {
            return new ResponseEntity<>(
                    authorRepository.getAuthorById(author_id),
                    HttpStatus.OK
            );
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }



    }

    public ResponseEntity<Collection<Author_entity>> getAuthors(Long offset, Long limit, String query) {

        return new ResponseEntity<>(
                authorRepository.getAuthors(offset, limit, query),
                HttpStatus.OK
        );
    }
}
