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
@Table(name = "game_questions")
public class GameQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String lyric;

    @ManyToOne
    @JoinColumn(name = "correct_song_id", nullable = false)
    @JsonIgnoreProperties({"category", "likes"})
    private Song correctSong;

    @Column(nullable = false)
    private String difficulty; // EASY, MEDIUM, HARD

    public GameQuestion() {
    }

    public GameQuestion(String lyric, Song correctSong, String difficulty) {
        this.lyric = lyric;
        this.correctSong = correctSong;
        this.difficulty = difficulty;
    }

    public Integer getId() {
        return id;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
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
