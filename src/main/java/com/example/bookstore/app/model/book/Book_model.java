package com.example.bookstore.app.model.book;

import lombok.Data;

import java.sql.Timestamp;



@Data
public class Book_model {

    private String title;
    private String description;
    private Long pages;
    private Long score_sum;
    private Long score_count;
    private String image;
    private Timestamp release;
    private Long author_id;
    private Long genre_id;
}
