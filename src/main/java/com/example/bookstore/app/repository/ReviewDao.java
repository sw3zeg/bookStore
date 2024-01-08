package com.example.bookstore.app.repository;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.Review.Review_model;
import com.example.bookstore.app.model.Review.Review_view;
import com.example.bookstore.app.rowmapper.Review_view_RowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class ReviewDao {

    private final NamedParameterJdbcTemplate db;


    public void createReview(Long customer_id, Long book_id, Review_model review) {

        String sql =   """
                                    insert into review
                                    (text, mark, customer_id, book_id)
                                    values
                                    (:text, :mark, :customer_id, :book_id)
                                    """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("text", review.getText())
                .addValue("mark", review.getMark())
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        try {
            db.update(sql, parameterSource);
        } catch (Exception e) {
            var errorMessage = e.getMessage();
            if (errorMessage.contains("ERROR: duplicate key value violates unique constraint")){
                throw new BadRequestException("You've already written review for book with id '%s'".formatted(book_id));
            } else if (errorMessage.contains("ERROR: insert or update on table")) {
                throw new BadRequestException("Book with id '%s' doesnt exists".formatted(book_id));
            }
        }
    }

    public void editReview(Long customer_id, Long book_id, Review_model review) {

        if (review.getMark() > 10 || review.getMark() < 0) {
            throw new BadRequestException("Mark not valid");
        }

        String sql =    """
                        update review
                        set text = :text,
                            mark = :mark,
                            updated = now()
                        where customer_id = :customer_id and book_id = :book_id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("text", review.getText())
                .addValue("mark", review.getMark())
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        int rowsUpdated = db.update(sql, parameterSource);

        if (rowsUpdated == 0) {
            throw new BadRequestException("Such review doesnt exists");
        }
    }


    public void deleteReview(Long customer_id, Long book_id) {
        String sql =    """
                        delete from review
                        where customer_id = :customer_id and book_id = :book_id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        int rowsUpdated = db.update(sql, parameterSource);

        if (rowsUpdated == 0) {
            throw new BadRequestException("Review with id '%s' doesnt exists".formatted(book_id));
        }
    }


    public Collection<Review_view> getReviewsOfBook(Long book_id, Long offset, Long limit) {
        String offset_sql = offset.toString().equals(AppConstants.OFFSET_DEFAULT_VALUE)
                ? ""
                : " offset :offset";
        String limit_sql = limit.toString().equals(AppConstants.LIMIT_DEFAULT_VALUE)
                ? ""
                : " limit :limit";

        String sql =    """
                        select * from review r
                        left join customer c on r.customer_id = c.id
                        where book_id = :book_id
                        """
                        + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("book_id", book_id)
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, new Review_view_RowMapper());
    }

}
