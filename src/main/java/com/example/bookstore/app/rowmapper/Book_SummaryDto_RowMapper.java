package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.book.Book_SummaryDto;
import com.example.bookstore.app.model.book.Book_view;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Book_SummaryDto_RowMapper implements RowMapper<Book_SummaryDto> {
    @Override
    public Book_SummaryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book_SummaryDto newBook = new Book_SummaryDto();
        newBook.setId(rs.getLong("id"));
        newBook.setTitle(rs.getString("title"));
        newBook.setImage(rs.getString("image"));
        newBook.setScore(rs.getDouble("score"));
        return newBook;
    }
}
