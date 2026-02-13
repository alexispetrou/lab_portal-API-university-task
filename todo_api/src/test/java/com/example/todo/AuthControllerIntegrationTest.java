package com.example.todo;

import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // Καθαρίζουμε τη βάση πριν κάθε test
        userRepository.deleteAll();
    }

    @Test
    void testSignupSuccess() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("1234");

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testSignupDuplicateUsername() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("1234");
        userRepository.save(user); // Υπάρχει ήδη

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("1234");
        userRepository.save(user);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue())); // επιστρέφει JWT
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("1234");
        userRepository.save(user);

        // Στέλνουμε λάθος password
        User wrongPass = new User();
        wrongPass.setUsername("testuser");
        wrongPass.setPassword("wrong");

        String json = objectMapper.writeValueAsString(wrongPass);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }
}
