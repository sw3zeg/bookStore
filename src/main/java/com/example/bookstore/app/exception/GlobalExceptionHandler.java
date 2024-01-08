package com.example.bookstore.app.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<AppError> handleBadRequestException(HttpServletRequest request, Exception e) {

        return new ResponseEntity<>(
                new AppError(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage()
                )
                , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
