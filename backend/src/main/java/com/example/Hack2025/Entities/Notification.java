package com.example.Hack2025.Entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {
    
    public enum NotificationType {
        STAR_RECEIVED,
        GROUP_INVITE,
        SONG_SHARED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"groups", "songs", "awards", "password", "email"})
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Optional: reference to who triggered the notification
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    @JsonIgnoreProperties({"groups", "songs", "awards", "password", "email"})
    private User fromUser;

    // Optional: reference to related group (for invites)
    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties({"members", "sharedSongs"})
    private Group relatedGroup;

    public Notification() {
    }

    public Notification(User user, NotificationType type, String message, User fromUser) {
        this.user = user;
        this.type = type;
        this.message = message;
        this.fromUser = fromUser;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Group getRelatedGroup() {
        return relatedGroup;
    }

    public void setRelatedGroup(Group relatedGroup) {
        this.relatedGroup = relatedGroup;
    }
}
