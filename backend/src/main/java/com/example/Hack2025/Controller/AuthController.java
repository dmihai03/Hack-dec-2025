package com.example.Hack2025.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Missing username or password");
        }

        try {
            User user = userRepo.getUserByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Simple password check (in production, use proper hashing!)
            if (!user.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }

            // Return user info (excluding password)
            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "name", user.getName(),
                "email", user.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

        if (name == null || username == null || email == null || password == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        // Check if username already exists
        try {
            User existingUser = userRepo.getUserByUsername(username).orElse(null);
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
        } catch (Exception e) {
            // User doesn't exist, which is what we want
        }

        // Check if email already exists
        try {
            User existingEmail = userRepo.getUserByEmail(email).orElse(null);
            if (existingEmail != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
        } catch (Exception e) {
            // Email doesn't exist, which is what we want
        }

        // Create new user
        User newUser = new User(name, username, email);
        newUser.setPassword(password); // In production, hash this!
        
        userRepo.addUser(newUser);

        // Return created user info
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "id", newUser.getId(),
            "username", newUser.getUsername(),
            "name", newUser.getName(),
            "email", newUser.getEmail()
        ));
    }
}
