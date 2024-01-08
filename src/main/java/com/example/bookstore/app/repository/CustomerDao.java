package com.example.bookstore.app.repository;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.customer.Customer_EditDto;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.rowmapper.CustomerEntity_RowMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CustomerDao {

    private final NamedParameterJdbcTemplate db;

//ok
    public Long createCustomer(Customer_model customer) {
        String sql =    """
                        insert into customer (email, username, password)
                        values(:email, :username, :password)
                        returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", customer.getEmail())
                .addValue("username", customer.getUsername())
                .addValue("password", customer.getPassword());

        try {
            return db.queryForObject(sql, parameterSource, Long.class);
        } catch (Exception e) {
            throw new BadRequestException("Customer with username '%s' already exists".formatted(customer.getUsername()));
        }
    }

//ok
    public void editCustomer(Customer_EditDto customer) {
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

        int rowsUpdated = db.update(sql, parameterSource);
        if (rowsUpdated == 0) {
            throw new BadRequestException("No customer found with ID " + customer.getId());
        }

    }

//ok
    public void deleteCustomer(Long customer_id) {
        String sql =    """
                        delete from customer
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", customer_id);

        int rowsUpdated = db.update(sql, parameterSource);
        if (rowsUpdated == 0) {
            throw new BadRequestException("No customer found with ID " + customer_id);
        }
    }

//to be deleted
    public Optional<Customer_entity> getCustomerById(Long customer_id) {
        String sql =    """
                        select * from customer
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", customer_id);

        try {
            Customer_entity response = db.queryForObject(sql, parameterSource, new CustomerEntity_RowMapper());
            return Optional.ofNullable(response);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
//ok
    public Customer_entity getCustomerByUsername(String username) {
        String sql =    """
                        select * from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        try {
            return db.queryForObject(sql, parameterSource, new CustomerEntity_RowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequestException("Customer with username '%s' doesnt exists".formatted(username));
        }
    }

//ok
    public Collection<Customer_entity> getCustomers(Long offset, Long limit, String query) {

        String offset_sql = offset.toString().equals(AppConstants.OFFSET_DEFAULT_VALUE)
                ? ""
                : " offset :offset";

        String limit_sql = limit.toString().equals(AppConstants.LIMIT_DEFAULT_VALUE)
                ? ""
                : " limit :limit";

        String query_sql = query.equals(AppConstants.QUERY_DEFAULT_VALUE)
                ? ""
                : " where customer.username like '%' || :query || '%'";

        String sql = "select * from customer order by username ASC" + query_sql + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("limit",limit)
                .addValue("offset",offset)
                .addValue("query",query);

        return db.query(sql, parameterSource, new CustomerEntity_RowMapper());
    }

    public Long addBalance(Long customerId, Long balance) {

        String sql =    """
                        update customer
                        set balance = customer.balance + :balance
                        where id = :id
                        returning balance
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", customerId)
                .addValue("balance", balance);

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    public Long indexOfCustomerByUsername(String username) {
        String sql =    """
                        select id from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource("username", username);

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    public void reduceBalance(Long customerId, Long balance) {
        String sql =    """
                        update customer
                        set balance = customer.balance - :balance
                        where id = :id
                        returning balance
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", customerId)
                .addValue("balance", balance);

        try {
            db.queryForObject(sql, parameterSource, Long.class);
        } catch (Exception e) {
            throw new BadRequestException("You have no money for this sale");
        }
    }
}
