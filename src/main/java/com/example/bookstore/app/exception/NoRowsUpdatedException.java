package com.example.bookstore.app.exception;

public class NoRowsUpdatedException extends RuntimeException {
    public NoRowsUpdatedException(String message) {
        super(message);
    }
}
