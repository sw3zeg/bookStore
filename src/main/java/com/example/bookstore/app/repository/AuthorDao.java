package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class AuthorDao {

    private final NamedParameterJdbcTemplate db;

    //POST
    public Long createAuthor(Author_model authorModel) {
        String sql = "insert into author (fio, biography, photo) values (:fio, :biography, :photo) returning id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("fio", authorModel.getFio())
                .addValue("biography", authorModel.getBiography())
                .addValue("photo", authorModel.getPhoto());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    //PUT
    public void editAuthor(Author_entity authorEntity) {
        String sql = "update author set fio = :fio, biography = :biography, photo = :photo where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", authorEntity.getId())
                .addValue("fio", authorEntity.getFio())
                .addValue("biography", authorEntity.getBiography())
                .addValue("photo", authorEntity.getPhoto());

        db.update(sql, parameterSource);
    }

    //DELETE
    public void deleteAuthor(Long author_id) {
        String sql = "delete from author where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", author_id);

        db.update(sql, parameterSource);
    }

    //GET
    public Author_entity getAuthorById(Long author_id) {
        String sql = "select * from author where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", author_id);

        return db.queryForObject(sql, parameterSource, (rs, rowNum) -> {
            Author_entity author = new Author_entity();
            author.setId(rs.getLong("id"));
            author.setFio(rs.getString("fio"));
            author.setBiography(rs.getString("biography"));
            author.setPhoto(rs.getString("photo"));
            return author;
        });
    }

    public List<Author_entity> getAuthors(Long offset, Long limit, String query) {
        query = '%' + query;
        query = query + '%';
        String sql = "select * from author where fio like :query"
                + (offset > 0 ? " offset :offset" : "")
                + (limit > 0 ? " limit :limit" : "")
                + " order by fio";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit)
                .addValue("query", query);

        return db.query(sql, parameterSource, (rs, rowNum) -> {
            Author_entity author = new Author_entity();
            author.setId(rs.getLong("id"));
            author.setFio(rs.getString("fio"));
            author.setBiography(rs.getString("biography"));
            author.setPhoto(rs.getString("photo"));
            return author;
        });
    }

}
