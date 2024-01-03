package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.CustomerRole.CustomerRole_entity;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.model.role.Role_entity;
import com.example.bookstore.app.model.role.Role_model;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRoleDao {

    private final NamedParameterJdbcTemplate db;

    public void addRoleToCustomer(CustomerRole_entity customerRole) {
        String sql =    """
                        insert into customer_role (customer_id, role_id)
                        values(:customer_id, :role_id)
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customerRole.getCustomer_id())
                .addValue("role_id", customerRole.getRole_id());

        db.update(sql, parameterSource);
    }

    public void deleteBookFromCustomer(CustomerRole_entity customerRole) {
        String sql =    """
                        delete from customer_role
                        where customer_id = :customer_id and role_id = :role_id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customerRole.getCustomer_id())
                .addValue("role_id", customerRole.getRole_id());

        db.update(sql, parameterSource);
    }

    public Collection<CustomerRole_entity> getRolesOfCustomer(Long customer_id, Long offset, Long limit) {
        String offset_sql = offset > 0 ? " offset :offset" : "";
        String limit_sql = limit > 0 ? " limit :limit" : "";
        String sql =    """
                        select * from customer_role cr
                        join role r on cr.role_id = r.id
                        join customer c on cr.customer_id = c.id
                        where c.id = :customer_id
                        """
                        + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            CustomerRole_entity customerRole = new CustomerRole_entity();
            customerRole.setCustomer_id(rs.getLong("customer_id"));
            customerRole.setRole_id(rs.getLong("role_id"));
            return customerRole;
        });
    }
}
