package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Long mark;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
}
