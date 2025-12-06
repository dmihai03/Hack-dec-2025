package com.example.Hack2025.Controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/groups")
public class GroupsController {
    @Autowired
    private UserRepository userRepo;

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
