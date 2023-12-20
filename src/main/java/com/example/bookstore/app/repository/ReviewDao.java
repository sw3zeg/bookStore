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

    //POST
    public Long createReview(Long book_id, Review_model review) {
        String createReview_sql = "insert into review (text, customer_id) values (:text, :customer_id) returning id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("text", review.getText())
                .addValue("customer_id", review.getCustomer_id());

        Long review_id = db.queryForObject(createReview_sql, parameterSource, Long.class);


        String addReviewToBook_sql = "insert into book_review (book_id, review_id) values (:book_id, :review_id)";

        parameterSource = new MapSqlParameterSource()
                .addValue("book_id", book_id)
                .addValue("review_id", review_id);

        db.update(addReviewToBook_sql, parameterSource);

        return review_id;
    }

    //DELETE
    public void deleteReview(Long book_id, Long review_id) {
        String sql = "delete from review where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", review_id);

        db.update(sql, parameterSource);
    }

    //GET
    public List<Review_view> getReviewsOfBook(Long book_id) {
        String sql = "select * from book_review right join review on book_review.review_id = review.id" +
                " left join customer on review.customer_id = customer.id where book_review.book_id = :book_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("book_id", book_id);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Review_view review = new Review_view();
            review.setId(rs.getLong("review_id"));
            review.setText(rs.getString("text"));
            review.setName(rs.getString("name"));
            review.setMail(rs.getString("mail"));
            return review;
        });
    }

}
