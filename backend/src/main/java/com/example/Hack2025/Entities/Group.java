package com.example.Hack2025.Entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private List<SharedSong> sharedSongs;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private List<GroupActivity> activities;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.sharedSongs = new ArrayList<>();
        this.activities = new ArrayList<>();
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

    public List<SharedSong> getSharedSongs() {
        return sharedSongs;
    }

    public void setSharedSongs(List<SharedSong> sharedSongs) {
        this.sharedSongs = sharedSongs;
    }

    public List<GroupActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<GroupActivity> activities) {
        this.activities = activities;
    }
}
