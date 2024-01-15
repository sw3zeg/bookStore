package com.example.bookstore.app.model.author;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author_entity {

    private Long id;
    private String fio;
    private String biography;
    private String photo;

    public boolean isValid() {
        return id > 0 && fio != null && biography != null && photo != null;
    }
}
