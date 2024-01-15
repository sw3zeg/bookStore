package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.customer.Customer_entity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerEntity_RowMapper implements RowMapper<Customer_entity> {
    @Override
    public Customer_entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer_entity customer = new Customer_entity();
        customer.setEmail(rs.getString("email"));
        customer.setUsername(rs.getString("username"));
        customer.setPassword(rs.getString("password"));
        customer.setBalance(rs.getLong("balance"));
        return customer;
    }
}
