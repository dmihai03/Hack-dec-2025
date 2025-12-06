package com.example.Hack2025.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Repositories.SongRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/songs")
@CrossOrigin(origins = "*")
public class SongsController {

    @Autowired
    private SongRepository songsRepo;

    @GetMapping("/")
    public ResponseEntity<?> getAllSongs() {
        return ResponseEntity.ok(songsRepo.getAllSongs());
    }

    @PostMapping("/{filter}")
    public ResponseEntity<?> getSongsByFilter(
        @PathVariable String filter,
        @RequestBody Map<String, String> body) {
        String query = body.get("query");
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing 'query' in request body");
        }
        return ResponseEntity.ok(songsRepo.getSongsByFilter(filter, query));
    }
}
