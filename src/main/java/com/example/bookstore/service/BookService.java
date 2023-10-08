package com.example.bookstore.service;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.exception.BookAlreadyExistException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@AllArgsConstructor
public class BookService
{

    private BookRepo bookRepo;

    public BookEntity addBook(BookEntity book) throws BookAlreadyExistException {
        if (bookRepo.findByTitle(book.getTitle()) != null)
        {
            throw new BookAlreadyExistException("Книга с таким именем уже существует");
        }
        return bookRepo.save(book);
    }


    public Book getOne(Long id) throws Exception {
        BookEntity book = bookRepo.findById(id).get();
        if (book == null)
        {
            throw new Exception("Книги с таким id не существует");
        }
        return Book.toModel(book);
    }


    public Long delete(Long id)
    {
        bookRepo.deleteById(id);
        return id;
    }


//    public void getAllBooks()
//    {
//        Iterable<BookEntity> a = bookRepo.findAll();
//    }
}
