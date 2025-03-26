package com.example.bookstore.app.model.customer;


import lombok.Data;

@Data
public class Customer_entity {

    private String email;
    private String username;
    private String password;
    private Long balance;
}
