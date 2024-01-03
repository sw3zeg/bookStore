package com.example.bookstore.app.service;


import com.example.bookstore.app.repository.GenreDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreDao genreDao;


}
