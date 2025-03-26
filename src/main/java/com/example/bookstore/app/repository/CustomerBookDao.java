package com.example.bookstore.app.repository;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.book.Book_SummaryDto;
import com.example.bookstore.app.rowmapper.Book_SummaryDto_RowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class CustomerBookDao {

    private final NamedParameterJdbcTemplate db;

    //POST
    public void addBookToCustomer(String username, Long book_id) {
        String sql =    """
                        insert into customer_book
                        (customer_username, book_id)
                        values
                        (:username, :book_id)
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("book_id", book_id);

        db.update(sql, parameterSource);
    }

    //DELETE
    public void deleteBookOfCustomer(Long customer_id, Long book_id) {
        String sql =    """
                        delete from customer_book
                        where  customer_id = :customer_id and book_id = :book_id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        db.update(sql, parameterSource);
    }

//ok
    public Collection<Book_SummaryDto> getBooksOfCustomer(String username, Long offset, Long limit) {

        String offset_sql = offset.toString().equals(AppConstants.OFFSET_DEFAULT_VALUE)
                ? ""
                : " offset :offset";

        String limit_sql = limit.toString().equals(AppConstants.LIMIT_DEFAULT_VALUE)
                ? ""
                : " limit :limit";


        String sql =    """
                        select  b.id,
                                b.title,
                                b.image,
                                round((score_sum/1.00) / score_count,1) as score
                        from customer_book cb
                        right Join book b on cb.book_id = b.id
                        left join author a on b.author_id = a.id
                        left join genre g on b.genre_id = g.id
                        left join customer c on c.id = cb.customer_id
                        where c.username = :username
                        """
                        + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, new Book_SummaryDto_RowMapper());

    }
}
