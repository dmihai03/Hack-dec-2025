package com.example.Hack2025.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(columnDefinition = "json")
    private List<String> awardArray;

    @Column(columnDefinition = "json")
    private List<String> groups;

    @Column(columnDefinition = "json")
    private List<String> songs;
    
    public User() {
    }
    
    public User(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.ratingNumber = 0;
        this.awardArray = List.of();
        this.groups = List.of();
        this.songs = List.of();
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

    public void addRatingNumber () {
        this.ratingNumber += 1;
    }

    public Integer getRatingNumber () {
        return ratingNumber;
    }

    public void setRatingNumber (Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public List<String> getAwardArray () {
        return awardArray;
    }

    public void setAwardArray (List<String> awardArray) {
        this.awardArray = awardArray;
    }

    public List<String> getGroups () {
        return groups;
    }

    public void setGroups (List<String> groups) {
        this.groups = groups;
    }

    public List<String> getSongs () {
        return songs;
    }

    public void setSongs (List<String> songs) {
        this.songs = songs;
    }
}
