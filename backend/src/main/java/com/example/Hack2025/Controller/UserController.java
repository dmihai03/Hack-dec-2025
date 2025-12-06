package com.example.Hack2025.Controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/{userID}/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable Integer userID) {
        return userRepo.getUserById(userID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userID}/songs/add")
    public ResponseEntity<?> addSongToUser(
        @PathVariable Integer userID,
        @RequestBody Map<String, Integer> body) {

        Integer songID = body == null ? null : body.get("songID");

        if (songID == null) {
            return ResponseEntity.badRequest().body("Missing 'songID' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepo.addSongToUser(userID, songID);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userID}/songs/remove")
    public ResponseEntity<?> removeSongFromUser(
        @PathVariable Integer userID,
        @RequestBody Map<String, Integer> body) {

        Integer songID = body == null ? null : body.get("songID");

        if (songID == null) {
            return ResponseEntity.badRequest().body("Missing 'songID' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepo.removeSongFromUser(userID, songID);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userID}/songs")
    public ResponseEntity<?> getUserSongs(@PathVariable Integer userID) {
        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepo.getUsersSongs(userID));
    }

    @GetMapping("/{userID}/groups")
    public ResponseEntity<?> getUserGroups(@PathVariable Integer userID) {
        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepo.getUserGroups(userID));
    }

    @GetMapping("/{userID}/awards")
    public ResponseEntity<?> getUserAwards(@PathVariable Integer userID) {
        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepo.getUserAwards(userID));
    }
}
