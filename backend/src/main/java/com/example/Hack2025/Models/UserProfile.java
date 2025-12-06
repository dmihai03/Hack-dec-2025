package com.example.Hack2025.Models;

import java.util.List;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;

public class UserProfile {
    private String userName;

    private Integer rating;

    private List<Integer> recentSongs;

    private List<Integer> badges;

    private List<Integer> groups;

    public UserProfile(Long id) {
        User user = new UserRepository().getUserById(id).orElse(null);
        
        userName = user.getUserName();

        rating = user.getRating();
    }
}