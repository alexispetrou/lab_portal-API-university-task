package com.example.todo;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoItem;
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
public class TodoJwtIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestAuthHelper testAuthHelper;

    private String token;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"1234\"}"))
                .andExpect(status().isOk());

        token = testAuthHelper.getToken("testuser", "1234");
    }

    @Test
    void testCreateAndGetTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("My first JWT todo");

        String json = objectMapper.writeValueAsString(todo);

        // Create Todo
        mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My first JWT todo"));

        // List all todos
        mockMvc.perform(get("/todos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testTodoItemCrud() throws Exception {
        // Δημιουργούμε ένα todo
        Todo todo = new Todo();
        todo.setTitle("Todo for items");
        String todoJson = objectMapper.writeValueAsString(todo);

        String response = mockMvc.perform(post("/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo createdTodo = objectMapper.readValue(response, Todo.class);

        // Δημιουργία TodoItem
        TodoItem item = new TodoItem();
        item.setDescription("First item");
        String itemJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(post("/todos/" + createdTodo.getId() + "/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("First item"));

        // Get TodoItem
        mockMvc.perform(get("/todos/" + createdTodo.getId() + "/items/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("First item"));

        // Update TodoItem
        item.setDescription("Updated item");
        item.setDone(true);
        String updatedJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(put("/todos/" + createdTodo.getId() + "/items/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated item"))
                .andExpect(jsonPath("$.done").value(true));

        // Delete TodoItem
        mockMvc.perform(delete("/todos/" + createdTodo.getId() + "/items/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
