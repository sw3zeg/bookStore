package com.example.bookstore.app.model.customer;


import lombok.Data;

@Data
public class Customer_entity {

    private Long id;
    private String mail;
    private String name;
    private String password;
}
