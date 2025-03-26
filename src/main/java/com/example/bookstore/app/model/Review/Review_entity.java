package com.example.bookstore.app.model.Review;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Review_entity {

    private String text;
    private Long mark;
    private Timestamp created;
    private Timestamp updated;
    private Long customer_id;
    private Long book_id;
}
