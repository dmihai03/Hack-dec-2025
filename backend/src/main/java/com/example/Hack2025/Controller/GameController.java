package com.example.Hack2025.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.GameQuestion;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Repositories.GameQuestionRepository;
import com.example.Hack2025.Repositories.SongRepository;

@RestController
@RequestMapping("/game")
public class GameController {
    
    @Autowired
    private GameQuestionRepository gameQuestionRepo;
    
    @Autowired
    private SongRepository songRepo;

    @GetMapping("/question")
    public ResponseEntity<?> getRandomQuestion() {
        GameQuestion question = gameQuestionRepo.getRandomQuestion();
        
        if (question == null) {
            return ResponseEntity.ok(Map.of(
                "error", "No questions available. Please add some questions to the database first."
            ));
        }

        // Get 3 random wrong answers
        List<Song> allSongs = songRepo.getAllSongs();
        List<Song> wrongAnswers = new ArrayList<>();
        
        for (Song song : allSongs) {
            if (!song.getId().equals(question.getCorrectSong().getId())) {
                wrongAnswers.add(song);
            }
        }
        
        Collections.shuffle(wrongAnswers);
        List<Song> options = new ArrayList<>();
        options.add(question.getCorrectSong());
        
        // Add up to 3 wrong answers
        for (int i = 0; i < Math.min(3, wrongAnswers.size()); i++) {
            options.add(wrongAnswers.get(i));
        }
        
        Collections.shuffle(options);

        return ResponseEntity.ok(Map.of(
            "id", question.getId(),
            "lyric", question.getLyric(),
            "options", options,
            "difficulty", question.getDifficulty()
        ));
    }

    @PostMapping("/seed")
    public ResponseEntity<?> seedQuestions() {
        // Get some songs from the database
        List<Song> songs = songRepo.getAllSongs();
        
        if (songs.size() < 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Need at least 4 songs in database to create questions"));
        }

        // Create sample questions with lyrics
        List<String> lyrics = List.of(
            "I'm walking on sunshine, whoa-oh",
            "Don't stop believin', hold on to that feeling",
            "We are the champions, my friends",
            "Sweet dreams are made of this",
            "I will always love you"
        );

        int created = 0;
        for (int i = 0; i < Math.min(lyrics.size(), songs.size()); i++) {
            GameQuestion question = new GameQuestion(
                lyrics.get(i),
                songs.get(i),
                "MEDIUM"
            );
            gameQuestionRepo.createQuestion(question);
            created++;
        }

        return ResponseEntity.ok(Map.of(
            "message", "Created " + created + " sample questions",
            "count", created
        ));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> checkAnswer(@RequestBody Map<String, Object> body) {
        Integer questionId = (Integer) body.get("questionId");
        Integer answerId = (Integer) body.get("answerId");

        GameQuestion question = gameQuestionRepo.getQuestionById(questionId);
        
        if (question == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question not found"));
        }

        boolean correct = question.getCorrectSong().getId().equals(answerId);
        
        return ResponseEntity.ok(Map.of(
            "correct", correct,
            "correctSong", Map.of(
                "id", question.getCorrectSong().getId(),
                "title", question.getCorrectSong().getTitle(),
                "artist", question.getCorrectSong().getArtist()
            )
        ));
    }
}
