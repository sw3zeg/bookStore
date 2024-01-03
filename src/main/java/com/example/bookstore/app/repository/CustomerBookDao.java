package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.book.Book_view;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CustomerBookDao {

    private final NamedParameterJdbcTemplate db;

    //POST
    public void addBookToCustomer(Long customer_id, Long book_id) {
        String sql = "insert into customer_book (customer_id, book_id)" +
                " values(:customer_id, :book_id)";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        db.update(sql, parameterSource);
    }

    //DELETE
    public void deleteBookFromCustomer(Long customer_id, Long book_id) {
        String sql = "delete from customer_book" +
                " where customer_id = :customer_id and book_id = :book_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        db.update(sql, parameterSource);
    }

    //GET
    public List<Book_view> getBooksFromCustomer(Long customer_id, Long offset, Long limit) {
        String offset_sql = offset > 0 ? " offset " + offset : "";
        String limit_sql = limit > 0 ? " limit " + limit : "";
        String sql =  "select *, round((score_sum/1.00) / score_count,1) as score, genre.title as genre_title" +
                " from customer_book right Join book on customer_book.book_id = book.id left join author " +
                "on book.author_id = author.id left join genre on book.genre_id = genre.id" +
                " where customer_id = :customer_id" + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource("customer_id", customer_id);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
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
            return newBook;
        });
    }
}
