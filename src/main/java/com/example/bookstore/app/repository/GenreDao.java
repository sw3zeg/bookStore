package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.genre.Genre_entity;
import com.example.bookstore.app.model.genre.Genre_model;
import com.example.bookstore.app.rowmapper.GenreEntity_RowMapper;
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

        String sql =    """
                        insert into genre (title)
                        values (:title)
                        returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("title", genre.getTitle());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    //DELETE
    public void deleteGenre(Long genre_id) {

        String sql = """
                     delete from genre
                     where id = :id
                     """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", genre_id);

        db.update(sql, parameterSource);
    }

    //GET
    public List<Genre_entity> getGenres(Long offset, Long limit) {

        String offsetSql = offset > 0 ? " offset :offset" : "";
        String limitSql = limit > 0 ? " limit :limit" : "";

        String sql =    """
                        select * from genre
                        order by title
                        """
                + offsetSql
                + limitSql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);

        return db.query(sql, parameterSource, new GenreEntity_RowMapper());
    }

}
