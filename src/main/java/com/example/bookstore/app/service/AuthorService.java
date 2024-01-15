package com.example.bookstore.app.service;

import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.repository.AuthorDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorDao authorRepository;

    public ResponseEntity<Long> createAuthor(Author_model author) {

        if (!author.isValid()) {
            throw new BadRequestException("Some field(s) not valid");
        }

        Long author_id = authorRepository.createAuthor(author);

        return new ResponseEntity<>(
                author_id,
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> editAuthor(Author_entity author) {

        if (!authorRepository.IsAuthorExists(author.getId())) {
            throw new BadRequestException("Author does not exists");
        }

        if (!author.isValid()) {
            throw new BadRequestException("Some field(s) not valid");
        }

        authorRepository.editAuthor(author);

        return new ResponseEntity<>(
                "Author '%s' was updated".formatted(author.getFio()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteAuthor(Long author_id) {

        if (!authorRepository.IsAuthorExists(author_id)) {
            throw new BadRequestException("Author does not exists");
        }

        authorRepository.deleteAuthor(author_id);

        return new ResponseEntity<>(
                "Author with id '%s' was deleted successfully".formatted(author_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Author_entity> getAuthorById(Long author_id) {

        Optional<Author_entity> author = authorRepository.getAuthorById(author_id);

        if (author.isEmpty()) {
            throw new BadRequestException("Author does not exists");
        }

        return new ResponseEntity<>(
                author.get(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Collection<Author_entity>> getAuthors(Long offset, Long limit, String query) {

        if (limit < 0 || offset < 0) {
            throw new BadRequestException("Some field(s) not valid");
        }

        Collection<Author_entity> authors = authorRepository.getAuthors(offset, limit, query);

        return new ResponseEntity<>(
                authors,
                HttpStatus.OK
        );
    }
}
