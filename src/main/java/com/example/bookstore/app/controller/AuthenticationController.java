package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.authentication.JwtRequest;
import com.example.bookstore.app.model.authentication.JwtResponse;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/authentication")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest authRequest) {
        return authenticationService.authenticate(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody Customer_model customer) {
        return authenticationService.createNewUser(customer);
    }

}
