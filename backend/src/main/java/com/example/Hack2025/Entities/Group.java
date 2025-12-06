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
@Table(name = "`groups`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "groups")
    @JsonIgnoreProperties("groups")
    private List<User> members;

    @ManyToMany
    @JoinTable(
        name = "groups_shared_songs",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> sharedSongs;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.sharedSongs = new ArrayList<>();
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

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Song> getSharedSongs() {
        return sharedSongs;
    }

    public void setSharedSongs(List<Song> sharedSongs) {
        this.sharedSongs = sharedSongs;
    }
}
