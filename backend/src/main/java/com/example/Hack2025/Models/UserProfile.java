package com.example.Hack2025.Models;

import java.util.List;
import com.example.Hack2025.Repositories.UserRepository;
import com.example.Hack2025.Entities.Award;
import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Entities.User;

public class UserProfile {
    private String userName;

    private Integer rating;

    private List<Song> songs;

    private List<Award> awards;

    private List<Group> groups;

    public UserProfile(Integer id) {
        User user = new UserRepository().getUserById(id).orElse(null);
        
        userName = user.getUsername();

        rating = user.getRatingNumber();

        songs = user.getSongs();

        groups = user.getGroups();

        awards = user.getAwards();
    }
}
