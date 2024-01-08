package com.example.bookstore.app.service;

import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.repository.AuthorDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorDao authorRepository;

    public ResponseEntity<Long> createAuthor(Author_model author) {

        return new ResponseEntity<>(
                authorRepository.createAuthor(author),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> editAuthor(Author_entity author) {

        authorRepository.editAuthor(author);

        return new ResponseEntity<>(
                "Author '%s' was updated".formatted(author.getFio()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteAuthor(Long author_id) {

        authorRepository.deleteAuthor(author_id);
        return new ResponseEntity<>(
                "Author with id '%s' was deleted".formatted(author_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Author_entity> getAuthorById(Long author_id) {

        return new ResponseEntity<>(
                authorRepository.getAuthorById(author_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Collection<Author_entity>> getAuthors(Long offset, Long limit, String query) {

        return new ResponseEntity<>(
                authorRepository.getAuthors(offset, limit, query),
                HttpStatus.OK
        );
    }
}
