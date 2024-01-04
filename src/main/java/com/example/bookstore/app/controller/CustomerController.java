package com.example.bookstore.app.controller;


import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.enums.Customer_sort;
import com.example.bookstore.app.repository.CustomerDao;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerDao dao;

    @PostMapping
    public Long createCustomer(@RequestBody Customer_model customer) {
        return dao.createCustomer(customer);
    }

    @PutMapping
    public void editCustomer(@RequestBody Customer_entity customer) {
        dao.editCustomer(customer);
    }

    @DeleteMapping("/{customer_id}")
    public void deleteCustomer(@PathVariable Long customer_id) {
        dao.deleteCustomer(customer_id);
    }

    @GetMapping("/{customer_id}")
    public Customer_entity getCustomerById(@PathVariable Long customer_id) {
        return dao.getCustomerById(customer_id);
    }

    @GetMapping
    public List<Customer_entity> getCustomers(
            @RequestParam(required = false, defaultValue = "-1") Long offset,
            @RequestParam(required = false, defaultValue = "-1") Long limit,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") Customer_sort sort
    ) {
        return dao.getCustomers(offset, limit, sort, query);
    }

    @PostMapping("/{customer_id}/books/{book_id}")
    public Long addBookToCustomer(
            @PathVariable Long customer_id,
            @PathVariable Long book_id
    ) {
        //return dao.addBookToCustomer(customer_id, book_id);
        return null;
    }

    @DeleteMapping("/{customer_id}/books/{book_id}")
    public void deleteBookFromCustomer(
            @PathVariable Long customer_id,
            @PathVariable Long book_id
    ) {
        //dao.deleteBookFromCustomer(customer_id, book_id);
    }

    @GetMapping("/{customer_id}/books")
    public List<Book_view> getBooksFromCustomer(
            @PathVariable Long customer_id,
            @RequestParam(required = false, defaultValue = "-1") Long offset,
            @RequestParam(required = false, defaultValue = "-1") Long limit
    ) {
        //return dao.getBooksFromCustomer(customer_id, offset, limit);
        return null;
    }

}
