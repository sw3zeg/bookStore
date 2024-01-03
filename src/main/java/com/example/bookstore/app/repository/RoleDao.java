package com.example.bookstore.app.repository;

import com.example.bookstore.app.model.role.Role_entity;
import com.example.bookstore.app.model.role.Role_model;
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

    public Long createRole(Role_model role) {
        String sql =    """
                        insert into role (name)
                        values (:name)
                        returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource("name", role.getName());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    public void deleteRole(Long id) {
        String sql =    """
                        delete from role
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        db.update(sql, parameterSource);
    }

    public Collection<Role_entity> getRoles(Long offset, Long limit) {
        String offset_sql = offset > 0 ? " offset :offset" : "";
        String limit_sql = limit > 0 ? " limit :limit" : "";
        String sql =    """
                        select * from role
                        order by name
                        """
                        + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Role_entity role = new Role_entity();
            role.setId(rs.getLong("id"));
            role.setName(rs.getString("name"));
            return role;
        });
    }

    public Collection<Role_entity> getRolesOFCustomer(Long customer_id) {
        String sql =    """
                        select * from role r
                        join customer_role cr on r.id = cr.role_id
                        where cr.customer_id = :customer_id
                        order by name
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("customer_id", customer_id);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Role_entity role = new Role_entity();
            role.setId(rs.getLong("id"));
            role.setName(rs.getString("name"));
            return role;
        });
    }

}
