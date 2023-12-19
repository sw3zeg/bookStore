package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.genre.Genre_entity;
import com.example.bookstore.app.model.genre.Genre_model;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class GenreDao {

    private final NamedParameterJdbcTemplate db;


    //POST
    public Long createGenre(Genre_model genre) {
        String sql = "insert into genre (title) values (:title) returning id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("title", genre.getTitle());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    //DELETE
    public void deleteGenre(Long genre_id) {
        String sql = "delete from genre where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", genre_id);

        db.update(sql, parameterSource);
    }

    //
    public List<Genre_entity> getGenres(Long offset, Long limit) {
        String sql = "select * from genre"
                + (offset > 0 ? " offset :offset" : "")
                + (limit > 0 ? " limit :limit" : "")
                + " order by title";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Genre_entity genre = new Genre_entity();
            genre.setId(rs.getLong("id"));
            genre.setTitle(rs.getString("title"));
            return genre;
        });
    }
}
