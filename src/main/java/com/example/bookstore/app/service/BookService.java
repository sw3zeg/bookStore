package com.example.bookstore.app.service;


import com.example.bookstore.app.enums.Book_sort;
import com.example.bookstore.app.exception.*;
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
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private BookDao bookRepository;
    private CustomerBookDao customerBookRepository;
    private CustomerService customerService;

    public ResponseEntity<Collection<Book_SummaryDto>> getBooksOfCustomer(
            Principal principal,
            Long offset,
            Long limit
    ) {

        String username = principal.getName();

        return new ResponseEntity<>(
                customerBookRepository.getBooksOfCustomer(username, offset, limit),
                HttpStatus.OK
        );
    }


    public ResponseEntity<Collection<Book_SummaryDto>> getBooks(
            Long offset,
            Long limit,
            String query,
            Book_sort sort
    ) {

        if (sort == null) sort = Book_sort.Release_ASC;

        return new ResponseEntity<>(
                bookRepository.getBooks(offset, limit, sort, query),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> createBook(Book_model book) {

        if (bookRepository.getBookByTitle(book.getTitle()).isPresent()) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            "Book with such title already exists"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                bookRepository.createBook(book),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> editBook(Book_entity book) {

        try {
            bookRepository.editBook(book);
            return new ResponseEntity<>(
                    "Book '%s' was created".formatted(book.getTitle()),
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

    public ResponseEntity<?> deleteBook(Long book_id) {

        try{
            bookRepository.deleteBook(book_id);
            return new ResponseEntity<>(
                    "Book '%s' was deleted".formatted(book_id),
                    HttpStatus.BAD_REQUEST
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

    public ResponseEntity<?> getBookById(Long book_id) {

        Optional<Book_view> book = bookRepository.getBookById(book_id);
        if (book.isPresent()) {
            return new ResponseEntity<>(
                    book.get(),
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                new AppError(
                        HttpStatus.BAD_REQUEST.value(),
                        "Book with id '%s' not found".formatted(book_id)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @Transactional
    public ResponseEntity<?> buyBook(Principal principal, Long book_id) {

        String username = principal.getName();

        try {

            Long customer_id = customerService.indexOfCustomerByName(username);

            Long price = bookRepository.getPriceOfBook(book_id);

            customerService.reduceBalance(customer_id, price);

            customerBookRepository.addBookToCustomer(customer_id, book_id);

        } catch (DuplicateException | BookNotFoundException | NegativeBalanceException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                String.format("Book with id '%s' was added to library to customer with username '%s'",book_id, username),
                HttpStatus.OK
        );
    }

}
