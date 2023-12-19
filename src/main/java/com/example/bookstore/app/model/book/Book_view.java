package com.example.bookstore.app.model.book;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Book_view {

    private Long id;
    private String title;
    private String description;
    private Long pages;
    private Double score;
    private String image;
    private Timestamp release;
    private String author_fio;
    private String genre_title;
}
