package com.example.Hack2025.Entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private Integer ratingNumber;

    private Integer coins;

    @Column(columnDefinition = "TEXT")
    private String ownedAvatars;

    @Column(columnDefinition = "TEXT")
    private String ownedPosters;

    @ManyToMany
    @JoinTable(
        name = "users_awards", // tabel intermediar
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "award_id")
    )
    private List<Award> awards;

    @ManyToMany
    @JoinTable(
        name = "users_groups",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnoreProperties("members")
    private List<Group> groups;

    @ManyToMany
    @JoinTable(
        name = "users_songs",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;
    
    public User() {
    }
    
    public User(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.ratingNumber = 0;
        this.awards = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.coins = 0;
        this.ownedAvatars = "default";
        this.ownedPosters = "aerosmith-rock";
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void addRatingNumber () {
        this.ratingNumber += 1;
    }

    public Integer getRatingNumber () {
        return ratingNumber;
    }

    public void setRatingNumber (Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public List<Award> getAwards () {
        return awards;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setAwards (List<Award> awards) {
        this.awards = awards;
    }

    public List<Group> getGroups () {
        return groups;
    }

    public void setGroups (List<Group> groups) {
        this.groups = groups;
    }

    public List<Song> getSongs () {
        return songs;
    }

    public void setSongs (List<Song> songs) {
        this.songs = songs;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public String getOwnedAvatars() {
        return ownedAvatars != null ? ownedAvatars : "default";
    }

    public void setOwnedAvatars(String ownedAvatars) {
        this.ownedAvatars = ownedAvatars;
    }

    public String getOwnedPosters() {
        return ownedPosters != null ? ownedPosters : "aerosmith-rock";
    }

    public void setOwnedPosters(String ownedPosters) {
        this.ownedPosters = ownedPosters;
    }
}
