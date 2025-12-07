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

    @GetMapping("/top/{limit}")
    public ResponseEntity<?> getTopUsersByRating(@PathVariable Integer limit) {
        return ResponseEntity.ok(userRepo.getTopUsersByRating(limit));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userRepo.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userID}/coins")
    public ResponseEntity<?> getUserCoins(@PathVariable Integer userID) {
        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("coins", userRepo.getUserCoins(userID)));
    }

    @PostMapping("/{userID}/coins/add")
    public ResponseEntity<?> addCoinsToUser(
        @PathVariable Integer userID,
        @RequestBody Map<String, Integer> body) {

        Integer amount = body == null ? null : body.get("amount");

        if (amount == null) {
            return ResponseEntity.badRequest().body("Missing 'amount' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepo.addCoinsToUser(userID, amount);
        return ResponseEntity.ok(Map.of("coins", userRepo.getUserCoins(userID)));
    }

    @GetMapping("/{userID}/avatars")
    public ResponseEntity<?> getOwnedAvatars(@PathVariable Integer userID) {
        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String owned = userRepo.getOwnedAvatars(userID);
        return ResponseEntity.ok(Map.of("ownedAvatars", owned != null ? owned : "default"));
    }

    @PostMapping("/{userID}/avatars/purchase")
    public ResponseEntity<?> purchaseAvatar(
        @PathVariable Integer userID,
        @RequestBody Map<String, Object> body) {

        String avatarId = body == null ? null : (String) body.get("avatarId");
        Integer cost = body == null ? null : (Integer) body.get("cost");

        if (avatarId == null || cost == null) {
            return ResponseEntity.badRequest().body("Missing 'avatarId' or 'cost' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        boolean success = userRepo.purchaseAvatar(userID, avatarId, cost);
        if (!success) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not enough coins"));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "coins", userRepo.getUserCoins(userID),
            "ownedAvatars", userRepo.getOwnedAvatars(userID)
        ));
    }

    @GetMapping("/{userID}/posters")
    public ResponseEntity<?> getOwnedPosters(@PathVariable Integer userID) {
        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String owned = userRepo.getOwnedPosters(userID);
        return ResponseEntity.ok(Map.of("ownedPosters", owned != null ? owned : "aerosmith-rock"));
    }

    @PostMapping("/{userID}/posters/purchase")
    public ResponseEntity<?> purchasePoster(
        @PathVariable Integer userID,
        @RequestBody Map<String, Object> body) {

        String posterId = body == null ? null : (String) body.get("posterId");
        Integer cost = body == null ? null : (Integer) body.get("cost");

        if (posterId == null || cost == null) {
            return ResponseEntity.badRequest().body("Missing 'posterId' or 'cost' in request body");
        }

        User user = userRepo.getUserById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        boolean success = userRepo.purchasePoster(userID, posterId, cost);
        if (!success) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not enough coins"));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "coins", userRepo.getUserCoins(userID),
            "ownedPosters", userRepo.getOwnedPosters(userID)
        ));
    }
}
