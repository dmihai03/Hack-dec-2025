package com.example.Hack2025.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "emoji_questions")
public class EmojiQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String emojis; // Store as string like "🍾💃🎶"

    @ManyToOne
    @JoinColumn(name = "correct_song_id", nullable = false)
    @JsonIgnoreProperties({"category", "likes"})
    private Song correctSong;

    @Column(nullable = false)
    private String difficulty; // EASY, MEDIUM, HARD

    public EmojiQuestion() {
    }

    public EmojiQuestion(String emojis, Song correctSong, String difficulty) {
        this.emojis = emojis;
        this.correctSong = correctSong;
        this.difficulty = difficulty;
    }

    public Integer getId() {
        return id;
    }

    public String getEmojis() {
        return emojis;
    }

    public void setEmojis(String emojis) {
        this.emojis = emojis;
    }

    public Song getCorrectSong() {
        return correctSong;
    }

    public void setCorrectSong(Song correctSong) {
        this.correctSong = correctSong;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
