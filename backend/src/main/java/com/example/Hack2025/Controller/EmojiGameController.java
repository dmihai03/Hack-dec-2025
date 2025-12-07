package com.example.Hack2025.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hack2025.Entities.EmojiQuestion;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Repositories.EmojiQuestionRepository;
import com.example.Hack2025.Repositories.SongRepository;

import java.util.List;

@RestController
@RequestMapping("/emoji-game")
public class EmojiGameController {
    
    @Autowired
    private EmojiQuestionRepository emojiQuestionRepo;
    
    @Autowired
    private SongRepository songRepo;

    @GetMapping("/question")
    public ResponseEntity<?> getRandomQuestion() {
        EmojiQuestion question = emojiQuestionRepo.getRandomQuestion();
        
        if (question == null) {
            return ResponseEntity.ok(Map.of(
                "error", "No emoji questions available. Please add some questions to the database first."
            ));
        }

        return ResponseEntity.ok(Map.of(
            "id", question.getId(),
            "emojis", question.getEmojis(),
            "difficulty", question.getDifficulty(),
            "correctSongId", question.getCorrectSong().getId()
        ));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> checkAnswer(@RequestBody Map<String, Object> body) {
        Integer questionId = (Integer) body.get("questionId");
        String answer = (String) body.get("answer");

        EmojiQuestion question = emojiQuestionRepo.getQuestionById(questionId);
        
        if (question == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question not found"));
        }

        Song correctSong = question.getCorrectSong();
        
        // Check if the answer matches the song title (case insensitive, trimmed)
        boolean correct = correctSong.getTitle().toLowerCase().trim()
                .equals(answer.toLowerCase().trim());
        
        return ResponseEntity.ok(Map.of(
            "correct", correct,
            "correctSong", Map.of(
                "id", correctSong.getId(),
                "title", correctSong.getTitle(),
                "artist", correctSong.getArtist()
            )
        ));
    }

    @PostMapping("/seed")
    public ResponseEntity<?> seedQuestions() {
        List<Song> songs = songRepo.getAllSongs();
        
        if (songs.size() < 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "Need at least 3 songs in database"));
        }

        // Create sample emoji questions
        String[][] emojiData = {
            {"😵🎸🔥", "Numb"},           // Linkin Park - Numb
            {"☠️💊🤮", "Toxicity"},        // System of a Down - Toxicity
            {"✈️🌌🛸", "Aerials"},         // System of a Down - Aerials
            {"💔😤💥", "Break Stuff"},     // Limp Bizkit - Break Stuff
            {"📝✍️🖤", "Blanck Space"},    // Taylor Swift - Blank Space
            {"🌄🏔️😈", "The Hills"},       // The Weeknd - The Hills
            {"🏙️🚶‍♂️🌃", "Strazile"},      // B.U.G. Mafia - Strazile
            {"👋🎤💔", "Hello"}            // Adele - Hello
        };

        int created = 0;
        for (int i = 0; i < Math.min(emojiData.length, songs.size()); i++) {
            EmojiQuestion question = new EmojiQuestion(
                emojiData[i][0],
                songs.get(i),
                "MEDIUM"
            );
            emojiQuestionRepo.createQuestion(question);
            created++;
        }

        return ResponseEntity.ok(Map.of(
            "message", "Created " + created + " emoji questions",
            "count", created
        ));
    }
}
