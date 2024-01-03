package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.book.Book_sort;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.model.customer.Customer_sort;
import com.example.bookstore.app.model.customer.Customer_view;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CustomerDao {

    private final NamedParameterJdbcTemplate db;

    public Long createCustomer(Customer_model customer) {
        String sql =    """
                        insert into customer (email, username, password)
                        values(:mail, :name, :password) returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("mail", customer.getEmail())
                .addValue("name", customer.getUsername())
                .addValue("password", customer.getPassword());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    public void editCustomer(Customer_entity customer) {
        String sql =    """
                        update customer
                        set email = :email,
                            username = :username,
                            password = :password
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", customer.getId())
                .addValue("email", customer.getEmail())
                .addValue("username", customer.getUsername())
                .addValue("password", customer.getPassword());

        db.update(sql, parameterSource);
    }

    //DELETE
    public void deleteCustomer(Long customer_id) {
        String sql =    """
                        delete from customer
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", customer_id);

        db.update(sql, parameterSource);
    }

    //GET
    public Customer_entity getCustomerById(Long customer_id) {
        String sql =    """
                        select * from customer
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", customer_id);

        return db.queryForObject(sql, parameterSource, (rs, rowNum) -> {
            Customer_entity customer = new Customer_entity();
            customer.setId(rs.getLong("id"));
            customer.setEmail(rs.getString("mail"));
            customer.setUsername(rs.getString("name"));
            customer.setPassword(rs.getString("password"));
            return customer;
        });
    }

    public Optional<Customer_view> getCustomerByUsername(String username) {
        String sql =    """
                        select * from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        try {
            Customer_view response = db.queryForObject(sql, parameterSource, (rs, rowNum) -> {
                Customer_view customer = new Customer_view();
                customer.setId(rs.getLong("id"));
                customer.setEmail(rs.getString("email"));
                customer.setUsername(rs.getString("username"));
                customer.setPassword(rs.getString("password"));
                return customer;
            });

            return Optional.ofNullable(response);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        //return Optional.ofNullable(response);
    }


    public List<Customer_entity> getCustomers(Long offset, Long limit, Customer_sort sort_type, String query) {

        if (sort_type == null) sort_type = Customer_sort.Name_ASC;//need remove

        String sort_sql = switch (sort_type) {
            case Email_ASC -> " order by customer.mail asc";
            case Email_DESC -> " order by customer.mail desc";
            case Name_ASC -> " order by customer.name asc";
            case Name_DESC -> " order by customer.name desc";
        };
        String offset_sql = offset > 0 ? " offset :offset" : "";
        String limit_sql = limit > 0 ? " limit :limit" : "";
        String query_sql = !query.isEmpty() ? " where customer.name like '%" + ":query" + "%'" : "";

        String sql = "select * from customer" + query_sql + sort_sql + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("limit",limit)
                .addValue("offset",offset)
                .addValue("query",query);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Customer_entity customer = new Customer_entity();
            customer.setId(rs.getLong("id"));
            customer.setEmail(rs.getString("mail"));
            customer.setUsername(rs.getString("name"));
            customer.setPassword(rs.getString("password"));
            return customer;
        });
    }
}
