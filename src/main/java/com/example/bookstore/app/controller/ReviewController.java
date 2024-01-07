package com.example.bookstore.app.controller;


import com.example.bookstore.app.enums.AppConstants;
import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

//ok
    @PostMapping("/private/books/{book_id}/reviews")
    public ResponseEntity<?> createReview(
            Principal principal,
            @PathVariable Long book_id,
            @RequestBody Review_model review
    ) {
        return reviewService.createReview(principal, book_id, review);
    }

//ok
    @PutMapping("/private/books/{book_id}/reviews")
    public ResponseEntity<?> editReview(
            Principal principal,
            @PathVariable Long book_id,
            @RequestBody Review_model review
    ) {
        return reviewService.editReview(principal, book_id, review);
    }

//ok
    @DeleteMapping("/private/books/{book_id}/reviews/")
    public ResponseEntity<?> deleteReview(
            Principal principal,
            @PathVariable Long book_id
    ) {
        return reviewService.deleteReview(principal, book_id);
    }

//ok
    @DeleteMapping("/admin/books/{book_id}/reviews/{customer_id}")
    public ResponseEntity<?> deleteReview_admin(
            @PathVariable Long book_id,
            @PathVariable Long customer_id

    ) {
        return reviewService.deleteReview(customer_id, book_id);
    }

//ok
    @GetMapping("/books/{book_id}/reviews")
    public ResponseEntity<Collection<Review_view>> getReviewsOfBook(
            @PathVariable Long book_id,
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit
    ) {
        return reviewService.getReviewsOfBook(book_id, offset, limit);
    }







}
