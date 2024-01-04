package com.example.bookstore.app.controller;


import com.example.bookstore.app.enums.AppConstants;
import com.example.bookstore.app.model.customer.Customer_EditDto;
import com.example.bookstore.app.model.customer.Customer_entity;
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


//ok
    @PutMapping("/admin/customers")
    public ResponseEntity<?> editCustomer(@RequestBody Customer_EditDto customer) {
        return customerService.editCustomer(customer);
    }

//ok
    @DeleteMapping("/admin/customers/{customer_id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customer_id) {
        return customerService.deleteCustomer(customer_id);
    }

//ok
    @GetMapping("/admin/customers")
    public ResponseEntity<Collection<Customer_entity>> getCustomers(
            @RequestParam(required = false, defaultValue = AppConstants.OFFSET_DEFAULT_VALUE) Long offset,
            @RequestParam(required = false, defaultValue = AppConstants.LIMIT_DEFAULT_VALUE) Long limit,
            @RequestParam(required = false, defaultValue = AppConstants.QUERY_DEFAULT_VALUE ) String query
    ) {
        return customerService.getCustomers(offset, limit, query);
    }

//ok
    @PostMapping("/admin/customers/{customer_id}/addBalance/{balance}")
    public ResponseEntity<?> addBalance(
            @PathVariable Long customer_id,
            @PathVariable Long balance
    ) {
        return customerService.addBalance(customer_id, balance);
    }

}
