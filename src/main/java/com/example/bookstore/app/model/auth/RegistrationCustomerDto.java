package com.example.bookstore.app.model.auth;


import lombok.Data;

@Data
public class RegistrationCustomerDto {

    private String username;
    private String email;
    private String password;
}
