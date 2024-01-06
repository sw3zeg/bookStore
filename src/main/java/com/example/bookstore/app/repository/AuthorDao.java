package com.example.bookstore.app.repository;


import com.example.bookstore.app.enums.AppConstants;
import com.example.bookstore.app.exception.NoRowsUpdatedException;
import com.example.bookstore.app.exception.ObjectNotFoundException;
import com.example.bookstore.app.exception.TooLargeFieldException;
import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.rowmapper.Author_entity_RowMapper;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@AllArgsConstructor
public class AuthorDao {

    private final NamedParameterJdbcTemplate db;

//ok
    public Long createAuthor(Author_model authorModel) throws TooLargeFieldException {
        String sql =    """
                        insert into author
                        (fio, biography, photo)
                        values
                        (:fio, :biography, :photo)
                        returning id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("fio", authorModel.getFio())
                .addValue("biography", authorModel.getBiography())
                .addValue("photo", authorModel.getPhoto());

        try {
            return db.queryForObject(sql, parameterSource, Long.class);
        } catch (Exception e) {
            throw new TooLargeFieldException("Some field too large");
        }
    }

//ok
    public void editAuthor(Author_entity authorEntity) throws TooLargeFieldException {
        String sql =    """
                        update author
                        set fio = :fio,
                            biography = :biography,
                            photo = :photo
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", authorEntity.getId())
                .addValue("fio", authorEntity.getFio())
                .addValue("biography", authorEntity.getBiography())
                .addValue("photo", authorEntity.getPhoto());

        try {
            db.update(sql, parameterSource);
        } catch (Exception e) {
            throw new TooLargeFieldException("Some field too large");
        }
    }

    //DELETE
    public void deleteAuthor(Long author_id) throws NoRowsUpdatedException {
        String sql =    """
                        delete from author
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", author_id);

        int rowsUpdated = db.update(sql, parameterSource);
        if (rowsUpdated == 0) {
            throw new NoRowsUpdatedException("No author found with ID " + author_id);
        }
    }

    //GET
    public Author_entity getAuthorById(Long author_id) throws ObjectNotFoundException {
        String sql =    """
                        select * from author
                        where id = :id
                        """;

        SqlParameterSource parameterSource = new MapSqlParameterSource
                ("id", author_id);

        try {
            return db.queryForObject(sql, parameterSource, new Author_entity_RowMapper());
        } catch (Exception e) {
            throw new ObjectNotFoundException("No author with id '%s'".formatted(author_id));
        }
    }

    public Collection<Author_entity> getAuthors(Long offset, Long limit, String query) {

        String offset_sql = offset.toString().equals(AppConstants.OFFSET_DEFAULT_VALUE)
                ? ""
                : " offset :offset";
        String limit_sql = limit.toString().equals(AppConstants.LIMIT_DEFAULT_VALUE)
                ? ""
                : " limit :limit";

        String sql =    """
                        select * from author
                        where fio ilike '%' || :query || '%'
                        order by fio
                        """ +offset_sql + limit_sql;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit)
                .addValue("query", query);

        return db.query(sql, parameterSource, new Author_entity_RowMapper());
    }

}
