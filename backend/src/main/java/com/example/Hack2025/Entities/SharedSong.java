package com.example.Hack2025.Entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "shared_songs")
public class SharedSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnoreProperties({"sharedSongs", "members"})
    private Group group;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnoreProperties({"groups", "songs", "awards", "password"})
    private User sender;

    @OneToMany(mappedBy = "sharedSong")
    @JsonIgnoreProperties({"sharedSong"})
    private List<SongStar> stars = new ArrayList<>();

    public SharedSong() {
    }

    public SharedSong(Song song, Group group, User sender) {
        this.song = song;
        this.group = group;
        this.sender = sender;
    }

    public Integer getId() {
        return id;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<SongStar> getStars() {
        return stars;
    }

    public void setStars(List<SongStar> stars) {
        this.stars = stars;
    }

    public int getStarCount() {
        return stars != null ? stars.size() : 0;
    }
}
