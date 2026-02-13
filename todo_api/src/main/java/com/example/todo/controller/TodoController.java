package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoItem;
import com.example.todo.repository.TodoItemRepository;
import com.example.todo.repository.TodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@Tag(name = "Todos", description = "Διαχείριση λίστας Todo και Todo Items")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {

    private final TodoRepository todoRepository;
    private final TodoItemRepository todoItemRepository;

    public TodoController(TodoRepository todoRepository, TodoItemRepository todoItemRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @GetMapping
    @Operation(summary = "Λίστα όλων των Todos", description = "Επιστρέφει όλα τα Todos μαζί με τα Items τους")
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Δημιουργία νέου Todo", description = "Δημιουργεί ένα νέο Todo με τίτλο")
    public Todo createTodo(@RequestBody Todo todo) {
        if (todo.getItems() != null) {
            todo.getItems().forEach(item -> item.setTodo(todo));
        }

        return todoRepository.save(todo);
    }

    @GetMapping("/{id}")@Operation(summary = "Λήψη ενός συγκεκριμένου Todo", description = "Επιστρέφει το Todo με το συγκεκριμένο id")
    public ResponseEntity<Todo> getTodo(@PathVariable Long id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Ενημέρωση ενός Todo", description = "Ενημερώνει τον τίτλο ενός Todo")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(updatedTodo.getTitle());
            return ResponseEntity.ok(todoRepository.save(todo));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Διαγραφή ενός Todo", description = "Διαγράφει ένα Todo και όλα τα Items του")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        return todoRepository.findById(id).map(todo -> {
            todoRepository.delete(todo);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/items/{iid}")
    @Operation(summary = "Λήψη ενός Todo Item", description = "Επιστρέφει ένα συγκεκριμένο Item για ένα Todo")
    public ResponseEntity<TodoItem> getTodoItem(@PathVariable Long id, @PathVariable Long iid) {
        return todoItemRepository.findById(iid)
                .filter(item -> item.getTodo().getId().equals(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Δημιουργία νέου Todo Item", description = "Δημιουργεί ένα νέο Item σε ένα συγκεκριμένο Todo")
    public ResponseEntity<TodoItem> createTodoItem(@PathVariable Long id, @RequestBody TodoItem item) {
        return todoRepository.findById(id).map(todo -> {
            item.setTodo(todo);
            return ResponseEntity.ok(todoItemRepository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/items/{iid}")
    @Operation(summary = "Ενημέρωση ενός Todo Item", description = "Ενημερώνει τα πεδία description και done ενός Item")
    public ResponseEntity<TodoItem> updateTodoItem(@PathVariable Long id, @PathVariable Long iid,
                                                   @RequestBody TodoItem updatedItem) {
        return todoItemRepository.findById(iid)
                .filter(item -> item.getTodo().getId().equals(id))
                .map(item -> {
                    item.setDescription(updatedItem.getDescription());
                    item.setDone(updatedItem.getDone());
                    return ResponseEntity.ok(todoItemRepository.save(item));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/items/{iid}")
    @Operation(summary = "Διαγραφή ενός Todo Item", description = "Διαγράφει ένα Item από το Todo")
    public ResponseEntity<?> deleteTodoItem(@PathVariable Long id, @PathVariable Long iid) {
        return todoItemRepository.findById(iid)
                .filter(item -> item.getTodo().getId().equals(id))
                .map(item -> {
                    todoItemRepository.delete(item);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
