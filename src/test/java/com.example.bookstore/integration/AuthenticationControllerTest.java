package com.example.bookstore.integration;


import com.example.bookstore.app.controller.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationController authenticationController;


    //==========//==========//==========//==========//==========//
    //AUTHENTICATE

    @Test
    @Sql(value = {"/customer/add_customers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/clear_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void authenticate_validPayload_ReturnJwtToken() throws Exception {

        mockMvc.perform(post("/api/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "admin",
                                    "password": "p"
                                }
"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }


    @Test
    public void authenticate_UsernameTooLarge_ReturnAppError() throws Exception {

        mockMvc.perform(post("/api/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "1234512345123451234512345123451234512345123451234512345",
                                    "password": "a"
                                }
"""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Some field is not valid")));
    }


    @Test
    public void authenticate_CustomerDoesNotExists_ReturnAppError() throws Exception {

        mockMvc.perform(post("/api/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "a",
                                    "password": "a"
                                }
"""))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Authentication failed")));
    }


    //==========//==========//==========//==========//==========//
    //AUTHENTICATE

    @Test
    @Sql(value = {"/customer/add_roles.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/clear_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createNewUser_validPayload_ReturnJwtToken() throws Exception {

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "andrey",
                                    "email": "andrey@andrey",
                                    "password": "password"
                                }
"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }


    @Test
    @Sql(value = {"/customer/add_roles.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/clear_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createNewUser_UsernameTooLarge_ThrowAppError() throws Exception {

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "1234512345123451234512345123451234512345123451234512345",
                                    "email": "andrey@andrey",
                                    "password": "password"
                                }
"""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Some field is not valid")));
    }

}
