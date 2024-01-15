package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.role.Role_entity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Role_entity_RowMapper implements RowMapper<Role_entity> {
    @Override
    public Role_entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role_entity role = new Role_entity();
        role.setName(rs.getString("name"));
        return role;
    }
}
