package com.example.bookstore.integration;


import com.example.bookstore.app.controller.AuthorController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.StringRegularExpression.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorController authorController;


    //==========//==========//==========//==========//==========//
    //CREATE AUTHOR

    @Test
    @Sql(value = {"/customer/add_customers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/clear_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin")
    public void createAuthor_validPayloadAndHaveAdminRole_ReturnId() throws Exception {

        mockMvc.perform(post("/api/admin/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fio": "Oleg",
                                  "biography": "was born in 2007",
                                  "photo": "https://pngimg.com/uploads/gull/gull_PNG44.png"
                                }
"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }


    @Test
    @Sql(value = {"/customer/add_customers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/clear_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("customer")
    public void createAuthor_HaveNoAdminRole_Return403Forbidden() throws Exception {

        mockMvc.perform(post("/api/admin/authors"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("You hav no access to this resource")));
    }


    @Test
    @Sql(value = {"/customer/add_customers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/clear_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin")
    public void createAuthor_invalidPayloadAndHaveAdminRole_ReturnId() throws Exception {

        mockMvc.perform(post("/api/admin/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fio": "Oleg",
                                  "biography": "was born in 2007"
                                }
"""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Some field(s) not valid"));
    }
}
