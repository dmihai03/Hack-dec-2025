package com.example.Hack2025.Controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.SharedSong;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.GroupRepository;
import com.example.Hack2025.Repositories.SharedSongRepository;
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

    @Autowired
    private SharedSongRepository sharedSongRepo;

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
        @RequestBody Map<String, Object> body) {
        
        String songTitle = body == null ? null : (String) body.get("songTitle");
        Integer senderId = body == null ? null : (Integer) body.get("senderId");
        
        if (songTitle == null || songTitle.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing 'songTitle' in request body");
        }
        
        if (senderId == null) {
            return ResponseEntity.badRequest().body("Missing 'senderId' in request body");
        }

        Group group = groupRepo.getGroupById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        Song song = songRepo.getSongByTitle(songTitle).orElse(null);
        if (song == null) {
            return ResponseEntity.badRequest().body("Song not found in database");
        }
        
        User sender = userRepo.getUserById(senderId).orElse(null);
        if (sender == null) {
            return ResponseEntity.badRequest().body("Sender not found");
        }

        songRepo.addSongToGroup(groupId, song.getId(), senderId);
        
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

    @PostMapping("/shared-songs/{sharedSongId}/star")
    public ResponseEntity<?> giveStar(
        @PathVariable Integer sharedSongId,
        @RequestBody Map<String, Integer> body) {
        
        Integer voterId = body == null ? null : body.get("voterId");
        
        if (voterId == null) {
            return ResponseEntity.badRequest().body("Missing 'voterId' in request body");
        }

        SharedSong sharedSong = sharedSongRepo.getSharedSongById(sharedSongId).orElse(null);
        if (sharedSong == null) {
            return ResponseEntity.badRequest().body("Shared song not found");
        }

        User voter = userRepo.getUserById(voterId).orElse(null);
        if (voter == null) {
            return ResponseEntity.badRequest().body("Voter not found");
        }

        // Check if user is trying to star their own song
        if (sharedSong.getSender().getId().equals(voterId)) {
            return ResponseEntity.badRequest().body("You cannot star your own shared song");
        }

        // Check if user already starred this song
        if (sharedSongRepo.hasUserStarred(sharedSongId, voterId)) {
            return ResponseEntity.badRequest().body("You have already starred this song");
        }

        sharedSongRepo.addStar(sharedSongId, voterId);

        return ResponseEntity.ok().body("Star given successfully");
    }

    @GetMapping("/shared-songs/{sharedSongId}/stars")
    public ResponseEntity<?> getStars(@PathVariable Integer sharedSongId) {
        SharedSong sharedSong = sharedSongRepo.getSharedSongById(sharedSongId).orElse(null);
        if (sharedSong == null) {
            return ResponseEntity.badRequest().body("Shared song not found");
        }

        int starCount = sharedSongRepo.getStarCount(sharedSongId);
        var voterIds = sharedSongRepo.getVoterIds(sharedSongId);

        return ResponseEntity.ok(Map.of(
            "starCount", starCount,
            "voterIds", voterIds
        ));
    }
}
