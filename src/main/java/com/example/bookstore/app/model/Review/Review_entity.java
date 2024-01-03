package com.example.bookstore.app.model.Review;

import lombok.Data;

@Data
public class Review_entity {

    private Long id;
    private String text;
    private Long customer_id;
    private Long book_id;
}
