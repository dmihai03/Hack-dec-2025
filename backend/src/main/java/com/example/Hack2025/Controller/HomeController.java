package com.example.Hack2025.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;

import jakarta.websocket.server.PathParam;

@RestController
public class HomeController {
    private final UserRepository userRepo = new UserRepository();

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return new String("Hello from home!");
    }

    @PostMapping("/user/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable Integer userID) {
        return userRepo.getUserById(userID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
}
