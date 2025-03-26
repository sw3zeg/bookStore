package com.example.bookstore.app.model.author;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author_model {

    private String fio;
    private String biography;
    private String photo;

    public boolean isValid() {
        return fio != null && biography != null && photo != null;
    }
}
