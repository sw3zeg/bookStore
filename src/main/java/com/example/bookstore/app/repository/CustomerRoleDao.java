package com.example.bookstore.app.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerRoleDao {

    private final NamedParameterJdbcTemplate db;

    public void addRoleToCustomer(String username, String role) {
        String sql =    """
                        insert into customer_role (customer_username, role_name)
                        values(:username, :role)
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("role", role);

        db.update(sql, parameterSource);
    }

}
