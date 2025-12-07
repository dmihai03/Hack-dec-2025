package com.example.Hack2025.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "song_stars", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"shared_song_id", "voter_id"})
})
public class SongStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "shared_song_id", nullable = false)
    @JsonIgnoreProperties({"stars", "group"})
    private SharedSong sharedSong;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    @JsonIgnoreProperties({"groups", "songs", "awards", "password"})
    private User voter;

    public SongStar() {
    }

    public SongStar(SharedSong sharedSong, User voter) {
        this.sharedSong = sharedSong;
        this.voter = voter;
    }

    public Integer getId() {
        return id;
    }

    public SharedSong getSharedSong() {
        return sharedSong;
    }

    public void setSharedSong(SharedSong sharedSong) {
        this.sharedSong = sharedSong;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }
}
