package com.example.bookstore.repository;

import com.example.bookstore.entity.FeedbackEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface FeedbackRepo extends CrudRepository<FeedbackEntity, Long> {

}
