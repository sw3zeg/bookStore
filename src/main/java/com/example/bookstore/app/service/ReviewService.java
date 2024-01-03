package com.example.bookstore.app.service;


import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.repository.ReviewDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewDao dao;

//    public Long createReview(Long book_id, Review_model review) {
//        try {
//            return dao.createReview(book_id, review);
//        }
//        catch () {
//
//        }
//    }

    public void deleteReview(Long review_id) {
        dao.deleteReview(review_id);
    }

    public List<Review_view> getReviewsOfBook(Long book_id, Long offset, Long limit) {
        return dao.getReviewsOfBook(book_id, offset, limit);
    }
}
