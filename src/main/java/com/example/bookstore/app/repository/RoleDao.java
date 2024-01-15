package com.example.bookstore.app.repository;

import com.example.bookstore.app.model.role.Role_entity;
import com.example.bookstore.app.rowmapper.Role_entity_RowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class RoleDao {

    private final NamedParameterJdbcTemplate db;


    public Collection<Role_entity> getRolesOFCustomer(String username) {
        String sql =    """
                        select * from role r
                        join customer_role cr on r.name = cr.role_name
                        where cr.customer_username = :username
                        order by name
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("username", username);

        return db.query(sql, parameterSource, new Role_entity_RowMapper());
    }

}
