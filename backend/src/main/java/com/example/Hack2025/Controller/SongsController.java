package com.example.Hack2025.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Repositories.SongRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/songs")
public class SongsController {

    @Autowired
    private SongRepository songsRepo;

    @GetMapping("/")
    public ResponseEntity<?> getAllSongs() {
        return ResponseEntity.ok(songsRepo.getAllSongs());
    }
}
