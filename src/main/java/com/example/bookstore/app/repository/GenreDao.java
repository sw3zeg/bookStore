package com.example.bookstore.app.repository;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.genre.Genre_entity;
import com.example.bookstore.app.model.genre.Genre_model;
import com.example.bookstore.app.rowmapper.GenreEntity_RowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class GenreDao {

    private final NamedParameterJdbcTemplate db;


    public Long createGenre(Genre_model genre) {
        String sql =    """
                        insert into genre (title)
                        values (:title)
                        returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("title", genre.getTitle());

        try {
            return db.queryForObject(sql, parameterSource, Long.class);
        } catch (Exception e) {
            throw new BadRequestException("Genre '%s' already exists".formatted(genre.getTitle()));
        }
    }

    public void deleteGenre(Long genre_id) {
        String sql = """
                     delete from genre
                     where id = :id
                     """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", genre_id);

        int rowsUpdated = db.update(sql, parameterSource);

        if (rowsUpdated == 0) {
            throw new BadRequestException("No genre found with ID " + genre_id);
        }
    }

    public Collection<Genre_entity> getGenres(Long offset, Long limit) {

        String offsetSql = offset.toString().equals(AppConstants.OFFSET_DEFAULT_VALUE)
                ? ""
                : " offset :offset";
        String limitSql = limit.toString().equals(AppConstants.LIMIT_DEFAULT_VALUE)
                ? ""
                : " limit :limit";

        String sql =    """
                        select * from genre
                        order by title
                        """
                        + offsetSql + limitSql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, new GenreEntity_RowMapper());
    }

}
