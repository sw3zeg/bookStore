package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.Review.Review_entity;
import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.repository.ReviewDao;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books/{book_id}/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewDao dao;

    @PostMapping
    public Long createReview(
            @PathVariable Long book_id,
            @RequestBody Review_model review
    ) {
        return dao.createReview(book_id, review);
    }

    @DeleteMapping("/{review_id}")
    public void deleteReview(
            @PathVariable Long book_id,
            @PathVariable Long review_id
    ) {
        dao.deleteReview(book_id, review_id);
    }

    @GetMapping
    public List<Review_view> getReviewsOfBook(
            @PathVariable Long book_id
    ) {
        return dao.getReviewsOfBook(book_id);
    }
}
