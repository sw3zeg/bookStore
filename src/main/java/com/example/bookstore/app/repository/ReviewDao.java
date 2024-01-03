package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.Review.Review_entity;
import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ReviewDao {

    private final NamedParameterJdbcTemplate db;


    public Long createReview(Long book_id, Review_model review) {
        String createReview_sql = "insert into review (text, customer_id, book_id)" +
                " values (:text, :customer_id, :book_id) returning id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("text", review.getText())
                .addValue("customer_id", review.getCustomer_id())
                .addValue("book_id", book_id);

        return db.queryForObject(createReview_sql, parameterSource, Long.class);
    }


    public void deleteReview(Long review_id) {
        String sql = "delete from review where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", review_id);

        db.update(sql, parameterSource);
    }


    public List<Review_view> getReviewsOfBook(Long book_id, Long offset, Long limit) {
        String offset_sql = offset > 0 ? " offset " + offset : "";
        String limit_sql = limit > 0 ? " limit " + limit : "";
        String sql = "select * from review left join customer on review.customer_id = customer.id" +
                " where book_id = :book_id" + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource("book_id", book_id);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Review_view review = new Review_view();
            review.setId(rs.getLong("id"));
            review.setText(rs.getString("text"));
            review.setName(rs.getString("name"));
            review.setMail(rs.getString("mail"));
            return review;
        });
    }

}
