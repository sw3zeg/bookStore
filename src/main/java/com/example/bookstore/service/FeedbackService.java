package com.example.bookstore.service;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.entity.FeedbackEntity;
import com.example.bookstore.repository.BookRepo;
import com.example.bookstore.repository.FeedbackRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FeedbackService {

    private FeedbackRepo feedbackRepo;
    private BookRepo bookRepo;

    public FeedbackEntity makeFeedback(FeedbackEntity entity, Long bookId){
        BookEntity book = bookRepo.findById(bookId).get();
        entity.setBook(book);
        return feedbackRepo.save(entity);
    }

    public FeedbackEntity compliteFeedback(Long id){
        FeedbackEntity feedback = feedbackRepo.findById(id).get();
        //
        return feedbackRepo.save(feedback);
    }
}
