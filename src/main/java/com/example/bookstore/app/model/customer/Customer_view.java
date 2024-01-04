package com.example.bookstore.app.model.customer;

import com.example.bookstore.app.model.role.Role_entity;
import lombok.Data;

import java.util.Collection;

@Data
public class Customer_view {

    private Long id;
    private String email;
    private String username;
    private String password;
    private Collection<Role_entity> roles;

    public Customer_view(Customer_entity customerEntity, Collection<Role_entity> roles) {
        this.id = customerEntity.getId();
        this.email = customerEntity.getEmail();
        this.username = customerEntity.getUsername();
        this.password = customerEntity.getPassword();
        this.roles = roles;
    }

    public Customer_view() {
    }
}
