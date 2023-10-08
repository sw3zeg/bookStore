package com.example.bookstore.model;

import com.example.bookstore.entity.BookEntity;
import lombok.Data;

@Data
public class Book {
    private Long id;
    private String title;

    public static Book toModel(BookEntity bookEntity)
    {
        Book book = new Book();
        book.setId(bookEntity.getId());
        book.setTitle(bookEntity.getTitle());
        return book;
    }
}
