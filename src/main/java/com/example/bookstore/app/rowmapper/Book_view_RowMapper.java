package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.book.Book_view;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Book_view_RowMapper implements RowMapper<Book_view> {
    @Override
    public Book_view mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book_view newBook = new Book_view();
        newBook.setId(rs.getLong("id"));
        newBook.setTitle(rs.getString("title"));
        newBook.setDescription(rs.getString("description"));
        newBook.setImage(rs.getString("image"));
        newBook.setRelease(rs.getTimestamp("release"));
        newBook.setPages(rs.getLong("pages"));
        newBook.setScore(rs.getDouble("score"));
        newBook.setAuthor_fio(rs.getString("fio"));
        newBook.setGenre_title(rs.getString("genre_title"));
        newBook.setPrice(rs.getLong("price"));
        return newBook;
    }
}
