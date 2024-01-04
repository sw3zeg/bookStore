package com.example.bookstore.app.repository;


import com.example.bookstore.app.enums.AppConstants;
import com.example.bookstore.app.model.book.Book_SummaryDto;
import com.example.bookstore.app.model.book.Book_entity;
import com.example.bookstore.app.model.book.Book_model;
import com.example.bookstore.app.enums.Book_sort;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.exception.BookNotFoundException;
import com.example.bookstore.app.exception.NoRowsUpdatedException;
import com.example.bookstore.app.rowmapper.Book_SummaryDto_RowMapper;
import com.example.bookstore.app.rowmapper.Book_view_RowMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookDao {

    private final NamedParameterJdbcTemplate db;


    public Long createBook(Book_model book) {
        String sql =    """
                        insert into book
                        (title, description, pages, score_sum, score_count, image,
                        release, author_id, genre_id, price)
                        values
                        (:title, :description, :pages, :score_sum, :score_count,
                        :image, :release, :author_id, :genre_id, :price)
                        returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("description", book.getDescription())
                .addValue("pages", book.getPages())
                .addValue("score_sum", book.getScore_sum())
                .addValue("score_count", book.getScore_count())
                .addValue("image", book.getImage())
                .addValue("release", book.getRelease())
                .addValue("author_id", book.getAuthor_id())
                .addValue("genre_id", book.getAuthor_id())
                .addValue("price", book.getPrice());

        return db.queryForObject(sql, parameterSource, Long.class);
    }


    public void editBook(Book_entity book) {
        String sql =    """
                        update book
                        set title = :title,
                            description = :description,
                            pages = :pages,
                            score_sum = :score_sum,
                            score_count = :score_count,
                            image = :image,
                            release = :release,
                            author_id = :author_id,
                            genre_id = :genre_id,
                            price = :price
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("description", book.getDescription())
                .addValue("pages", book.getPages())
                .addValue("score_sum", book.getScore_sum())
                .addValue("score_count", book.getScore_count())
                .addValue("image", book.getImage())
                .addValue("release", book.getRelease())
                .addValue("author_id", book.getAuthor_id())
                .addValue("genre_id", book.getGenre_id())
                .addValue("price", book.getPrice());

        int rowsUpdated = db.update(sql, parameterSource);

        if (rowsUpdated == 0) {
            throw new NoRowsUpdatedException("No book found with title " + book.getTitle());
        }
    }


    public void deleteBook(Long book_id) throws NoRowsUpdatedException {
        String sql =    """
                        delete from book
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", book_id);

        int rowsUpdated = db.update(sql, parameterSource);

        if (rowsUpdated == 0) {
            throw new NoRowsUpdatedException("No book found with id " + book_id);
        }
    }


    public Optional<Book_view> getBookById(Long book_id) {
        String sql =    """
                        select  *,
                                round((score_sum/1.00) / score_count,1) as score,
                                g.title as genre_title
                        from book b
                        left join author a on b.author_id = a.id
                        left join genre g on b.genre_id = g.id
                        where b.id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", book_id);

        try {
            Book_view response = db.queryForObject(sql, parameterSource, new Book_view_RowMapper());
            return Optional.ofNullable(response);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Book_view> getBookByTitle(String title) {
        String sql =    """
                        select  *,
                                round((score_sum/1.00) / score_count,1) as score,
                                g.title as genre_title
                        from book b
                        left join author a on b.author_id = a.id
                        left join genre g on b.genre_id = g.id
                        where b.title = :title
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource("title", title);

        try {
            Book_view response =  db.queryForObject(sql, parameterSource, new Book_view_RowMapper());
            return Optional.ofNullable(response);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


//ok
    public List<Book_SummaryDto> getBooks(Long offset, Long limit, Book_sort sort_type, String query) {

        String sort_sql = switch (sort_type) {
            case Score_ASC -> " order by score asc";
            case Score_DESC -> " order by score desc";
            case Title_ASC -> " order by b.title asc";
            case Title_DESC -> " order by b.title desc";
            case Release_ASC -> " order by b.release asc";
            case Release_DESC -> " order by b.release desc";
        };
        String offset_sql = offset.toString().equals(AppConstants.OFFSET_DEFAULT_VALUE)
                ? ""
                : " offset :offset";
        String limit_sql = limit.toString().equals(AppConstants.LIMIT_DEFAULT_VALUE)
                ? ""
                : " limit :limit";
        String query_sql = query.isEmpty()
                ? ""
                : " where b.title like '%' || :query || '%'";

        String sql =    """
                        select  b.id,
                                b.title,
                                b.image,
                                round((score_sum/1.00) / score_count,1) as score
                        from book b
                        left join author a on b.author_id = a.id
                        left join genre g on b.genre_id = g.id
                        """ + query_sql + sort_sql + offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit)
                .addValue("query", query);

        return db.query(sql, parameterSource, new Book_SummaryDto_RowMapper());
    }

    public Long getPriceOfBook(Long book_id) throws BookNotFoundException {
        String sql =    """
                        select price from book
                        where id = :id
                        """;

        SqlParameterSource  parameterSource = new MapSqlParameterSource
                ("id", book_id);

        try {
            return db.queryForObject(sql, parameterSource, Long.class);
        }catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException(String.format("Book with id '%s' not found", book_id));
        }
    }
}
