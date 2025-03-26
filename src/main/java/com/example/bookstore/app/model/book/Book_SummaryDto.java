package com.example.bookstore.app.model.book;

import lombok.Data;

@Data
public class Book_SummaryDto {

    private Long id;
    private String title;
    private Double score;
    private String image;
}
