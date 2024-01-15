package com.example.bookstore.app.service;


import com.example.bookstore.app.enums.Book_sort;
import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.book.Book_SummaryDto;
import com.example.bookstore.app.model.book.Book_entity;
import com.example.bookstore.app.model.book.Book_model;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.repository.BookDao;
import com.example.bookstore.app.repository.CustomerBookDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;

@Service
@AllArgsConstructor
public class BookService {

    private BookDao bookRepository;
    private CustomerBookDao customerBookRepository;
    private CustomerService customerService;

    public ResponseEntity<Collection<Book_SummaryDto>> getBooksOfCustomer(Principal principal, Long offset, Long limit) {

        String username = principal.getName();

        return new ResponseEntity<>(
                customerBookRepository.getBooksOfCustomer(username, offset, limit),
                HttpStatus.OK
        );
    }


    public ResponseEntity<Collection<Book_SummaryDto>> getBooks(Long offset, Long limit, String query, Book_sort sort) {

        if (sort == null) sort = Book_sort.Release_ASC;

        return new ResponseEntity<>(
                bookRepository.getBooks(offset, limit, sort, query),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Long> createBook(Book_model book) {

        if (!book.isValid()) {
            throw new BadRequestException("Some field(s) not valid");
        }

        return new ResponseEntity<>(
                bookRepository.createBook(book),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> editBook(Book_entity book) {

        if (!book.isValid()) {
            throw new BadRequestException("Some field(s) not valid");
        }

        bookRepository.editBook(book);
        return new ResponseEntity<>(
                "Book '%s' was created".formatted(book.getTitle()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteBook(Long book_id) {

        bookRepository.deleteBook(book_id);
        return new ResponseEntity<>(
                "Book '%s' was deleted".formatted(book_id),
                HttpStatus.BAD_REQUEST
        );
    }

    public ResponseEntity<Book_view> getBookById(Long book_id) {

        return new ResponseEntity<>(
                bookRepository.getBookById(book_id),
                HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<String> buyBook(Principal principal, Long book_id) {

        String username = principal.getName();

        Long price = bookRepository.getPriceOfBook(book_id);

        customerService.reduceBalance(username, price);

        customerBookRepository.addBookToCustomer(username, book_id);

        return new ResponseEntity<>(
                String.format("Book with id '%s' was added to library to customer with username '%s'",book_id, username),
                HttpStatus.OK
        );
    }

}
