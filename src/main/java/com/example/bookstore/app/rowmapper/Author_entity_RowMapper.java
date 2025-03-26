package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.author.Author_entity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Author_entity_RowMapper implements RowMapper<Author_entity> {

    @Override
    public Author_entity mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Author_entity.builder()
                .id(rs.getLong("id"))
                .fio(rs.getString("fio"))
                .biography(rs.getString("biography"))
                .photo(rs.getString("photo"))
                .build();
    }
}
