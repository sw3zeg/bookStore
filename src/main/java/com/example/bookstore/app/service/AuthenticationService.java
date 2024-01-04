package com.example.bookstore.app.service;


import com.example.bookstore.app.exception.AppError;
import com.example.bookstore.app.model.authentication.JwtRequest;
import com.example.bookstore.app.model.authentication.JwtResponse;
import com.example.bookstore.app.model.customer.Customer_model;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;

    public ResponseEntity<?> authenticate(JwtRequest authRequest) throws BadCredentialsException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    ));

            UserDetails userDetails = customerService.loadUserByUsername(authRequest.getUsername());
            String token =  jwtTokenService.generateToken(userDetails);

            return new ResponseEntity<>(
                    new JwtResponse(token),
                    HttpStatus.OK
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            "Invalid login or password"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    public ResponseEntity<?> createNewUser(Customer_model customer) {
        if (customerService.findByUsername(customer.getUsername()).isPresent()) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            "Customer with such username already exists"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        customerService.createNewUser(customer);

        UserDetails userDetails = customerService.loadUserByUsername(customer.getUsername());
        return new ResponseEntity<>(
                new JwtResponse (jwtTokenService.generateToken(userDetails)),
                HttpStatus.OK
        );
    }

}
