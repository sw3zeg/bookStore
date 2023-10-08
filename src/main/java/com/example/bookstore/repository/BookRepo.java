package com.example.bookstore.repository;

import com.example.bookstore.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepo extends CrudRepository<BookEntity, Long>
{

    public BookEntity findByTitle(String title);

}
