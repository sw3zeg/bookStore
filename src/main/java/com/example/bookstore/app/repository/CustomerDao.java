package com.example.bookstore.app.repository;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.rowmapper.CustomerEntity_RowMapper;
import lombok.AllArgsConstructor;
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


    public Boolean isCustomerExists(String username) {
        String sql =    """
                        select count(*) from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        Long rows = db.queryForObject(sql, parameterSource, Long.class);
        return rows == 1L;
    }

    public void createCustomer(Customer_model customer) {
        String sql =    """
                        insert into customer (email, username, password)
                        values(:email, :username, :password)
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", customer.getEmail())
                .addValue("username", customer.getUsername())
                .addValue("password", customer.getPassword());

        db.update(sql, parameterSource);
    }


    public void editCustomer(Customer_model customer) {
        String sql =    """
                        update customer
                        set email = :email,
                            password = :password
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", customer.getEmail())
                .addValue("username", customer.getUsername())
                .addValue("password", customer.getPassword());

        db.update(sql, parameterSource);
    }


    public void deleteCustomer(String username) {
        String sql =    """
                        delete from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        db.update(sql, parameterSource);
    }

    public Optional<Customer_entity> getCustomerByUsername(String username) {
        String sql =    """
                        select * from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        Customer_entity response = db.queryForObject(sql, parameterSource, new CustomerEntity_RowMapper());
        return Optional.ofNullable(response);
    }


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

    public Long addBalance(String username, Long balance) {

        String sql =    """
                        update customer
                        set balance = customer.balance + :balance
                        where username = :username
                        returning balance
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("balance", balance);

        return db.queryForObject(sql, parameterSource, Long.class);
    }

//    public Long indexOfCustomerByUsername(String username) {
//        String sql =    """
//                        select id from customer
//                        where username = :username
//                        """;
//
//        SqlParameterSource parameterSource = new MapSqlParameterSource("username", username);
//
//        return db.queryForObject(sql, parameterSource, Long.class);
//    }

    public void reduceBalance(String username, Long balance) {
        String sql =    """
                        update customer
                        set balance = customer.balance - :balance
                        where username = :username
                        returning balance
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("balance", balance);

        db.queryForObject(sql, parameterSource, Long.class);
    }

    public Long getBalance(String username) {
        String sql =    """
                        select balance from customer
                        where username = :username
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        return db.queryForObject(sql, parameterSource, Long.class);
    }
}
