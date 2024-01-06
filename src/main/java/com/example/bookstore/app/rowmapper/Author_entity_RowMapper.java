package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.author.Author_entity;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Author_entity_RowMapper implements RowMapper<Author_entity> {

    @Override
    public Author_entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author_entity author = new Author_entity();
        author.setId(rs.getLong("id"));
        author.setFio(rs.getString("fio"));
        author.setBiography(rs.getString("biography"));
        author.setPhoto(rs.getString("photo"));
        return author;
    }
}
