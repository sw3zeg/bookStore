package com.example.bookstore.app.model.customer;


import lombok.Data;

@Data
public class Customer_EditDto {

    private Long id;
    private String email;
    private String username;
    private String password;
}
