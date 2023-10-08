package com.example.bookstore.controller;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.exception.BookAlreadyExistException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.repository.BookRepo;
import com.example.bookstore.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    BookService bookService;

    @PostMapping
    public ResponseEntity addBook(@RequestBody BookEntity book)
    {
        try
        {
            bookService.addBook(book);
            return ResponseEntity.ok("Книга успешно добавлена");
        }
        catch (BookAlreadyExistException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Прозошла ошибка");
        }
    }

    @GetMapping
    public ResponseEntity getOneBook(@RequestParam Long id)
    {
        try
        {
            return ResponseEntity.ok(bookService.getOne(id));
        }
        catch (BookNotFoundException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Прозошла ошибка");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id)
    {
        try
        {
            return ResponseEntity.ok(bookService.delete(id));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Прозошла ошибка");
        }
    }


//    @GetMapping
//    public ResponseEntity getAllBooks()
//    {
//        try
//        {
//            return ResponseEntity.ok("ok. 200");
//        }
//        catch (Exception e)
//        {
//            return ResponseEntity.badRequest().body("Прозошла ошибка");
//        }
//    }
}
