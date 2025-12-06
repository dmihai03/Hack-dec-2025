package com.example.Hack2025.Models;

import java.util.List;
import com.example.Hack2025.Repositories.UserRepository;
import com.example.Hack2025.Entities.Award;
import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.Songs;
import com.example.Hack2025.Entities.User;

public class UserProfile {
    private String userName;

    private Integer rating;

    private List<Songs> recentSongs;

    private List<Award> badges;

    private List<Group> groups;

    public UserProfile(Long id) {
        User user = new UserRepository().getUserById(id).orElse(null);
        
        userName = user.getUsername();

        rating = user.getRatingNumber();

        

    }
}
