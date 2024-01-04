package com.example.bookstore.app.model.authentication;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;
    private String password;
}
