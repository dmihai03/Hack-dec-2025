package com.example.Hack2025.Controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.GroupRepository;
import com.example.Hack2025.Repositories.SongRepository;
import com.example.Hack2025.Repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/groups")
public class GroupsController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private SongRepository songRepo;

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupDetails(@PathVariable Integer groupId) {
        Group group = groupRepo.getGroupById(groupId).orElse(null);
        
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/share-song")
    public ResponseEntity<?> shareSongToGroup(
        @PathVariable Integer groupId,
        @RequestBody Map<String, String> body) {
        
        String songTitle = body == null ? null : body.get("songTitle");
        
        if (songTitle == null || songTitle.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing 'songTitle' in request body");
        }

        Group group = groupRepo.getGroupById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        Song song = songRepo.getSongByTitle(songTitle).orElse(null);
        if (song == null) {
            return ResponseEntity.badRequest().body("Song not found in database");
        }

        songRepo.addSongToGroup(groupId, song.getId());
        
        return ResponseEntity.ok().body("Song shared successfully");
    }

    @PostMapping("/{groupID}/add")
    public ResponseEntity<?> addUserToGroup(
        @PathVariable Integer groupID,
        @RequestBody Map<String, Integer> body) {

        Integer userID = body == null ? null : body.get("userID");

        if (userID == null) {
            return ResponseEntity.badRequest().body("Missing 'userID' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepo.addGroupToUser(userID, groupID);

        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{groupID}/remove")
    public ResponseEntity<?> removeUserFromGroup(
        @PathVariable Integer groupID,
        @RequestBody Map<String, Integer> body) {
        
        Integer userID = body == null ? null : body.get("userID");
        
        if (userID == null) {
            return ResponseEntity.badRequest().body("Missing 'userID' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepo.removeGroupFromUser(userID, groupID);

        return ResponseEntity.ok().build();
    }
}
