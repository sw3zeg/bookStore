package com.example.bookstore.entity;



import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<FeedbackEntity> feedbacks;
}
