package com.example.bookstore.app.controller;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PutMapping("/admin/customers")
    public ResponseEntity<String> editCustomer(@RequestBody Customer_model customer) {
        return customerService.editCustomer(customer);
    }

    @DeleteMapping("/admin/customers/{username}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String username) {
        return customerService.deleteCustomer(username);
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<Collection<Customer_entity>> getCustomers(
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit,
            @RequestParam(required = false, defaultValue = AppConstants.QUERY_DEFAULT_VALUE ) String query
    ) {
        return customerService.getCustomers(offset, limit, query);
    }

    @PostMapping("/admin/customers/{username}/addBalance/{balance}")
    public ResponseEntity<String> addBalance(
            @PathVariable String username,
            @PathVariable Long balance
    ) {
        return customerService.addBalance(username, balance);
    }

}
