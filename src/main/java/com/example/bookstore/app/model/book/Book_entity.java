package com.example.bookstore.app.model.book;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Book_entity {

    private Long id;
    private String title;
    private String description;
    private Long pages;
    private Long score_sum;
    private Long score_count;
    private String image;
    private Timestamp release;
    private Long author_id;
    private Long genre_id;
    private Long price;

    public boolean isValid() {
        return  id != null &&
                title != null &&
                description != null &&
                pages != null &&
                score_sum != null &&
                score_count != null &&
                image != null &&
                release != null &&
                author_id != null &&
                genre_id != null &&
                price != null;
    }

}
