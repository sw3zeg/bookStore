package com.example.bookstore.app.model.customer;

import com.example.bookstore.app.model.role.Role_entity;
import lombok.Data;

import java.util.Collection;

@Data
public class Customer_view {

    private String email;
    private String username;
    private String password;
    private Long balance;
    private Collection<Role_entity> roles;
}
