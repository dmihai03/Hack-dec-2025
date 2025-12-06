package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.example.Hack2025.Entities.User;

import java.util.Optional;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    // Get user by ID
    public Optional<User> getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }
}
