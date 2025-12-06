package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Song;

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
}
