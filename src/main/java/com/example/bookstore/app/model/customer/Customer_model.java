package com.example.bookstore.app.model.customer;

import lombok.Data;

@Data
public class Customer_model {

    private String email;
    private String username;
    private String password;

    public boolean isValid() {
        return email != null && password != null && username != null && username.length() <= 50;
    }
}
