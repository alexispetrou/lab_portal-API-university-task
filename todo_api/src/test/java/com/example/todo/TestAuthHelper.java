package com.example.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestAuthHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public String getToken(String username, String password) throws Exception {
        String userJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(userJson))
                .andReturn();

        return result.getResponse().getContentAsString();
    }
}
