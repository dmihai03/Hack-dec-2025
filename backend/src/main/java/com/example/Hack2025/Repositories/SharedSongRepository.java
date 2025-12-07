package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Notification;
import com.example.Hack2025.Entities.SharedSong;
import com.example.Hack2025.Entities.SongStar;
import com.example.Hack2025.Entities.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SharedSongRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<SharedSong> getSharedSongById(Integer id) {
        SharedSong sharedSong = entityManager.find(SharedSong.class, id);
        return Optional.ofNullable(sharedSong);
    }

    public boolean hasUserStarred(Integer sharedSongId, Integer voterId) {
        String query = "SELECT COUNT(s) FROM SongStar s WHERE s.sharedSong.id = :sharedSongId AND s.voter.id = :voterId";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("sharedSongId", sharedSongId)
                .setParameter("voterId", voterId)
                .getSingleResult();
        return count > 0;
    }

    public void addStar(Integer sharedSongId, Integer voterId) {
        SharedSong sharedSong = entityManager.find(SharedSong.class, sharedSongId);
        User voter = entityManager.find(User.class, voterId);
        
        if (sharedSong != null && voter != null) {
            SongStar star = new SongStar(sharedSong, voter);
            entityManager.persist(star);
            
            // Increment the sender's rating
            User sender = sharedSong.getSender();
            if (sender != null) {
                Integer currentRating = sender.getRatingNumber();
                sender.setRatingNumber(currentRating == null ? 1 : currentRating + 1);
                entityManager.merge(sender);

                // Create notification for the sender
                String songTitle = sharedSong.getSong().getTitle();
                String message = "@" + voter.getUsername() + " gave you a ⭐ for sharing \"" + songTitle + "\"";
                Notification notification = new Notification(sender, Notification.NotificationType.STAR_RECEIVED, message, voter);
                entityManager.persist(notification);
            }
        }
    }

    public int getStarCount(Integer sharedSongId) {
        String query = "SELECT COUNT(s) FROM SongStar s WHERE s.sharedSong.id = :sharedSongId";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("sharedSongId", sharedSongId)
                .getSingleResult();
        return count.intValue();
    }

    public List<Integer> getVoterIds(Integer sharedSongId) {
        String query = "SELECT s.voter.id FROM SongStar s WHERE s.sharedSong.id = :sharedSongId";
        return entityManager.createQuery(query, Integer.class)
                .setParameter("sharedSongId", sharedSongId)
                .getResultList();
    }
}
