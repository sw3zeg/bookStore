package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.genre.Genre_entity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreEntity_RowMapper implements RowMapper<Genre_entity> {
    @Override
    public Genre_entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre_entity genre = new Genre_entity();
        genre.setId(rs.getLong("id"));
        genre.setTitle(rs.getString("title"));
        return genre;
    }
}
