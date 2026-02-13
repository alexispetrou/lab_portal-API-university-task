package com.example.todo.controller;

import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication", description = "Endpoints για signup, login και logout")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Δημιουργία νέου χρήστη",
            description = "Δημιουργεί νέο χρήστη με username και password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", description = "Username already exists")
            }
    )
    public ResponseEntity<?> signup(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "Login χρήστη",
            description = "Επιστρέφει JWT token αν τα credentials είναι σωστά",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Επιτυχημένο login"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    public ResponseEntity<?> login(@RequestBody User user) {
        return userRepository.findByUsername(user.getUsername())
                .filter(u -> u.getPassword().equals(user.getPassword()))
                .map(u -> {
                    String token = jwtUtil.generateToken(u.getUsername());
                    return ResponseEntity.ok(token);
                })
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }

    @GetMapping("/auth/logout")
    @Operation(
            summary = "Logout χρήστη",
            description = "Stateless JWT – δεν υπάρχει πραγματικό logout, επιστρέφει επιτυχία",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logged out successfully")
            }
    )
    public ResponseEntity<?> logout() {
        //δεν διαγραφεται το token
        return ResponseEntity.ok("Logged out successfully");
    }
}