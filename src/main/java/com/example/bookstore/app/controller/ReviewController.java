package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.Review.Review_entity;
import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.repository.ReviewDao;
import com.example.bookstore.app.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/books/{book_id}/reviews")
    public Long createReview(
            @PathVariable Long book_id,
            @RequestBody Review_model review
    ) {
        //return reviewService.createReview(book_id, review);
    return null;
    }

    @DeleteMapping("/books/reviews/{review_id}")
    public void deleteReview(
            @PathVariable Long review_id
    ) {
        reviewService.deleteReview(review_id);
    }

    @GetMapping("/books/{book_id}/reviews")
    public List<Review_view> getReviewsOfBook(
            @PathVariable Long book_id,
            @RequestParam(required = false, defaultValue = "-1") Long offset,
            @RequestParam(required = false, defaultValue = "-1") Long limit
    ) {
        return reviewService.getReviewsOfBook(book_id, offset, limit);
    }
}
