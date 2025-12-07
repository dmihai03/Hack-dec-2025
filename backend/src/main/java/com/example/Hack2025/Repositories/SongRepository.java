package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.SharedSong;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Entities.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SongRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Song> getSongById(Integer id) {
        Song song = entityManager.find(Song.class, id);
        return Optional.ofNullable(song);
    }

    public Optional<Song> getSongByTitle(String title) {
        String query = "SELECT s FROM Song s WHERE s.title = :title";
        List<Song> results = entityManager.createQuery(query, Song.class)
                .setParameter("title", title)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Song> getSongsByArtist(String artist) {
        String query = "SELECT s FROM Song s WHERE s.artist = :artist";
        return entityManager.createQuery(query, Song.class)
                .setParameter("artist", artist)
                .getResultList();
    }

    public List<Song> getSongsByCategory(String category) {
        String query = "SELECT s FROM Song s WHERE s.category = :category";
        return entityManager.createQuery(query, Song.class)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Song> getAllSongs() {
        String query = "SELECT s FROM Song s";
        return entityManager.createQuery(query, Song.class).getResultList();
    }

    public void addSong(Song song) {
        entityManager.persist(song);
    }

    public void deleteSong(Integer songId) {
        Song song = entityManager.find(Song.class, songId);
        if (song != null) {
            entityManager.remove(song);
        }
    }

    public Object getSongsByFilter(String filter, String value) {
        switch (filter.toLowerCase()) {
            case "title":
                return getSongByTitle(value);
            case "artist":
                return getSongsByArtist(value);
            case "category":
                return getSongsByCategory(value);
            default:
                return ResponseEntity.badRequest().body("Invalid filter: " + filter);
        }
    }

    public void addSongToGroup(Integer groupId, Integer songId, Integer senderId) {
        Group group = entityManager.find(Group.class, groupId);
        Song song = entityManager.find(Song.class, songId);
        User sender = entityManager.find(User.class, senderId);
        
        if (group != null && song != null && sender != null) {
            // Check if this song was already shared to this group
            String checkQuery = "SELECT ss FROM SharedSong ss WHERE ss.group.id = :groupId AND ss.song.id = :songId";
            List<SharedSong> existing = entityManager.createQuery(checkQuery, SharedSong.class)
                    .setParameter("groupId", groupId)
                    .setParameter("songId", songId)
                    .getResultList();
            
            if (existing.isEmpty()) {
                SharedSong sharedSong = new SharedSong(song, group, sender);
                entityManager.persist(sharedSong);
            }
        }
    }

    public void removeSongFromGroup(Integer groupId, Integer songId) {
        String query = "DELETE FROM SharedSong ss WHERE ss.group.id = :groupId AND ss.song.id = :songId";
        entityManager.createQuery(query)
                .setParameter("groupId", groupId)
                .setParameter("songId", songId)
                .executeUpdate();
    }

    public List<SharedSong> getGroupSharedSongs(Integer groupId) {
        String query = "SELECT ss FROM SharedSong ss WHERE ss.group.id = :groupId";
        return entityManager.createQuery(query, SharedSong.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
