package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.book.Book_entity;
import com.example.bookstore.app.model.book.Book_model;
import com.example.bookstore.app.enums.Book_sort;
import com.example.bookstore.app.model.book.Book_view;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BookDao {

    private final NamedParameterJdbcTemplate db;


    //POST
    public Long createBook(Book_model book) {
        String sql = "insert into book (title, description, pages, score_sum," +
                " score_count, image, release, author_id, genre_id)" +
                "values (:title, :description, :pages, :score_sum," +
                " :score_count, :image, :release, :author_id, :genre_id) returning id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("description", book.getDescription())
                .addValue("pages", book.getPages())
                .addValue("score_sum", book.getScore_sum())
                .addValue("score_count", book.getScore_count())
                .addValue("image", book.getImage())
                .addValue("release", book.getRelease())
                .addValue("author_id", book.getAuthor_id())
                .addValue("genre_id", book.getGenre_id());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    //PUT
    public void editBook(Book_entity book) {
        String sql = "update book set title = :title, description = :description, pages = :pages," +
                " score_sum = :score_sum, score_count = :score_count, image = :image," +
                " release = :release, author_id = :author_id, genre_id = :genre_id" +
                " where id = :id";

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
                .addValue("genre_id", book.getGenre_id());

        db.update(sql, parameterSource);
    }

    //DELETE
    public void deleteBook(Long book_id) {
        String sql = "delete from book where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", book_id);

        db.update(sql, parameterSource);
    }

    //GET
    public Book_view getBookById(Long book_id) {
        String sql = "select *, round((score_sum/1.00) / score_count,1) as score, genre.title as genre_title" +
                " from book left join author on book.author_id = author.id" +
                " left join genre on book.genre_id = genre.id where book.id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", book_id);

        return db.queryForObject(sql, parameterSource, (rs, rowNum) -> {
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

    public List<Book_view> getBooks(Long offset, Long limit, Book_sort sort_type, String query) {
        if (sort_type == null) sort_type = Book_sort.Release_ASC;
        String sort_sql = switch (sort_type) {
            case Score_ASC -> " order by score asc";
            case Score_DESC -> " order by score desc";
            case Title_ASC -> " order by book.title asc";
            case Title_DESC -> " order by book.title desc";
            case Release_ASC -> " order by book.release asc";
            case Release_DESC -> " order by book.release desc";
        };
        String offset_sql = offset > 0 ? " offset "+offset : "";
        String limit_sql = limit > 0 ? " limit "+limit : "";
        String query_sql = !query.isEmpty() ? " like '%" + query + "%'": "";

        String sql = "select *, round((score_sum/1.00) / score_count,1) as score, genre.title as genre_title" +
                " from book left join author on book.author_id = author.id" +
                " left join genre on book.genre_id = genre.id" +
                query_sql + sort_sql + offset_sql + limit_sql;

        return db.query(sql, (rs, rowNum) -> {
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
