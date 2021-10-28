package com.exist.exercise08.services.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exist.exercise08.model.user.User;
import com.exist.exercise08.services.data.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthControllerTest.class)
public class AuthControllerTest {

    @MockBean
    UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_sign_up_succesfully() throws Exception{
        User user = new User("username", "email@email.com", "password12");


        mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/auth/signup") 
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
        ;
    }
}
