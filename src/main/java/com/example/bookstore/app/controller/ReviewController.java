package com.example.bookstore.app.controller;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/private/books/{book_id}/reviews")
    public ResponseEntity<?> createReview(
            Principal principal,
            @PathVariable Long book_id,
            @RequestBody Review_model review
    ) {
        return reviewService.createReview(principal, book_id, review);
    }

    @PutMapping("/private/books/{book_id}/reviews")
    public ResponseEntity<String> editReview(
            Principal principal,
            @PathVariable Long book_id,
            @RequestBody Review_model review
    ) {
        return reviewService.editReview(principal, book_id, review);
    }

    @DeleteMapping("/private/books/{book_id}/reviews/")
    public ResponseEntity<String> deleteReview(
            Principal principal,
            @PathVariable Long book_id
    ) {
        return reviewService.deleteReview(principal, book_id);
    }

    @DeleteMapping("/admin/books/{book_id}/reviews/{username}")
    public ResponseEntity<String> deleteReview_admin(
            @PathVariable Long book_id,
            @PathVariable String username

    ) {
        return reviewService.deleteReview(username, book_id);
    }

    @GetMapping("/books/{book_id}/reviews")
    public ResponseEntity<Collection<Review_view>> getReviewsOfBook(
            @PathVariable Long book_id,
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit
    ) {
        return reviewService.getReviewsOfBook(book_id, offset, limit);
    }
}
