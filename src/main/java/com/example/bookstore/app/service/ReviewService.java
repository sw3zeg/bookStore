package com.example.bookstore.app.service;


import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.repository.ReviewDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewDao reviewRepository;
    private final CustomerService customerService;


    public ResponseEntity<String> createReview(Principal principal, Long book_id, Review_model review) {

        String username = principal.getName();
        Long customer_id = customerService.indexOfCustomerByName(username);
        reviewRepository.createReview(customer_id, book_id, review);

        return new ResponseEntity<>(
                "Review for book with id '%s' has been written".formatted(book_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> editReview(Principal principal, Long book_id, Review_model review) {

        String username = principal.getName();
        Long customer_id = customerService.indexOfCustomerByName(username);
        reviewRepository.editReview(customer_id, book_id, review );

        return new ResponseEntity<>(
                "Review for book with id '%s' has been updated".formatted(book_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteReview(Principal principal, Long book_id) {

        String username = principal.getName();
        Long customer_id = customerService.indexOfCustomerByName(username);
        reviewRepository.deleteReview(customer_id, book_id);

        return new ResponseEntity<>(
                "Review for book with id '%s' has been deleted".formatted(book_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteReview(Long customer_id, Long book_id) {

        reviewRepository.deleteReview(customer_id, book_id);

        return new ResponseEntity<>(
                "Review for book with id '%s' has been deleted".formatted(book_id),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Collection<Review_view>> getReviewsOfBook(Long book_id, Long offset, Long limit) {

        return new ResponseEntity<>(
                reviewRepository.getReviewsOfBook(book_id, offset, limit),
                HttpStatus.OK
        );
    }
}
