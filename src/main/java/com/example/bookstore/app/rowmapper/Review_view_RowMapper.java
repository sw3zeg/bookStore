package com.example.bookstore.app.rowmapper;

import com.example.bookstore.app.model.Review.Review_view;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Review_view_RowMapper implements RowMapper<Review_view> {

    @Override
    public Review_view mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review_view review = new Review_view();
        review.setText(rs.getString("text"));
        review.setMark(rs.getLong("mark"));
        review.setCreated(rs.getTimestamp("created"));
        review.setUpdated(rs.getTimestamp("updated"));
        review.setUsername(rs.getString("username"));
        return review;
    }
}
