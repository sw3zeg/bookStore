package com.example.bookstore.app.service;


import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.authentication.JwtRequest;
import com.example.bookstore.app.model.authentication.JwtResponse;
import com.example.bookstore.app.model.customer.Customer_model;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;

    @Autowired
    public AuthenticationService(JwtTokenService jwtTokenService, AuthenticationManager authenticationManager, CustomerService customerService) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
    }


    public ResponseEntity<JwtResponse> authenticate(JwtRequest authRequest) {

        if (!authRequest.isValid()) {
            throw new BadRequestException("Some field is not valid");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtTokenService.generateToken(userDetails);

        return new ResponseEntity<>(
                new JwtResponse(jwtToken),
                HttpStatus.OK
        );
    }


    public ResponseEntity<JwtResponse> createNewUser(Customer_model customer) {

        if (!customer.isValid()) {
            throw new BadRequestException("Some field is not valid");
        }

        customerService.createNewUser(customer);

        UserDetails userDetails = customerService.loadUserByUsername(customer.getUsername());
        String jwtToken = jwtTokenService.generateToken(userDetails);

        return new ResponseEntity<>(
                new JwtResponse(jwtToken),
                HttpStatus.OK
        );
    }

}
