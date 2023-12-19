package com.example.bookstore.app.repository;


import com.example.bookstore.app.model.book.Book_sort;
import com.example.bookstore.app.model.book.Book_view;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.model.customer.Customer_sort;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CustomerDao {

    private final NamedParameterJdbcTemplate db;

    //POST
    public Long createCustomer(Customer_model customer) {
        String sql = "insert into customer (mail, name, password) values(:mail, :name, :password) returning id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("mail", customer.getMail())
                .addValue("name", customer.getName())
                .addValue("password", customer.getPassword());

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    //PUT
    public void editCustomer(Customer_entity customer) {
        String sql = "update customer set mail = :mail, name = :name, password = :password where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", customer.getId())
                .addValue("mail", customer.getMail())
                .addValue("name", customer.getName())
                .addValue("password", customer.getPassword());

        db.update(sql, parameterSource);
    }

    //DELETE
    public void deleteCustomer(Long customer_id) {
        String sql = "delete from customer where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", customer_id);

        db.update(sql, parameterSource);
    }

    //GET
    public Customer_entity getCustomerById(Long customer_id) {
        String sql = "select * from customer where id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", customer_id);

        return db.queryForObject(sql, parameterSource, (rs, rowNum) -> {
            Customer_entity customer = new Customer_entity();
            customer.setId(rs.getLong("id"));
            customer.setMail(rs.getString("mail"));
            customer.setName(rs.getString("name"));
            customer.setPassword(rs.getString("password"));
            return customer;
        });
    }

    public List<Customer_entity> getCustomers(Long offset, Long limit, Customer_sort sort_type, String query) {
        if (sort_type == null) sort_type = Customer_sort.Name_ASC;
        String sort_sql = switch (sort_type) {
            case Mail_ASC -> " order by mail asc";
            case Mail_DESC -> " order by mail desc";
            case Name_ASC -> " order by customer.name asc";
            case Name_DESC -> " order by customer.name desc";
        };
        String offset_sql = offset > 0 ? " offset " + offset : "";
        String limit_sql = limit > 0 ? " limit " + limit : "";
        String query_sql = !query.isEmpty() ? " where customer.name like '%" + query + "%'" : "";

        String sql = "select * from customer" + query_sql + sort_sql + offset_sql + limit_sql;

        return db.query(sql, (rs, rowNum) -> {
            Customer_entity customer = new Customer_entity();
            customer.setId(rs.getLong("id"));
            customer.setMail(rs.getString("mail"));
            customer.setName(rs.getString("name"));
            customer.setPassword(rs.getString("password"));
            return customer;
        });
    }

    //BOOK LIBRARY
    public Long addBookToCustomer(Long customer_id, Long book_id) {
        String sql = "insert into customer_book (customer_id, book_id) values(:customer_id, :book_id) returning book_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        return db.queryForObject(sql, parameterSource, Long.class);
    }

    public void deleteBookFromCustomer(Long customer_id, Long book_id) {
        String sql = "delete from customer_book where customer_id = :customer_id and book_id = :book_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", customer_id)
                .addValue("book_id", book_id);

        db.update(sql, parameterSource);
    }

    public List<Book_view> getBooksFromCustomer(Long customer_id, Long offset, Long limit) {
        String offset_sql = offset > 0 ? " offset " + offset : "";
        String limit_sql = limit > 0 ? " limit " + limit : "";
        String sql =  "select *, round((score_sum/1.00) / score_count,1) as score, genre.title as genre_title" +
                " from customer_book left Join book on customer_book.book_id = book.id left join author " +
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
