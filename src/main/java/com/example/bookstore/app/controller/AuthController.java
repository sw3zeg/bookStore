package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.auth.AppError;
import com.example.bookstore.app.model.auth.JwtRequest;
import com.example.bookstore.app.model.auth.RegistrationCustomerDto;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.service.CustomerService;
import com.example.bookstore.app.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;


    @PostMapping("/public/auth")
    public String s(@RequestBody JwtRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        ));

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = customerService.loadUserByUsername(authRequest.getUsername());
            return jwtTokenService.generateToken(userDetails);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }


    @PostMapping("/public/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationCustomerDto registrationUserModel) {
//        if (customerService.loadUserByUsername(registrationUserModel.getUsername()) != null) {
//            return new ResponseEntity<>(
//                    new AppError(
//                            HttpStatus.BAD_REQUEST.value(),
//                            "Такой пользователь уже существует"
//                    ),
//                    HttpStatus.BAD_REQUEST
//            );
//        }

        customerService.createNewUser(registrationUserModel);

        UserDetails userDetails = customerService.loadUserByUsername(registrationUserModel.getUsername());
        return ResponseEntity.ok(jwtTokenService.generateToken(userDetails));
    }

    @PostMapping("/public/test")
    public String tes() {
        return "TEST";
    }

    @PostMapping("/test")
    public String tesa() {
        return "ECURED TEST";
    }
}
