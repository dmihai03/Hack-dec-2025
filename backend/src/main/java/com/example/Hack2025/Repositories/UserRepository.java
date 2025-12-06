package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.Songs;
import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Entities.Award;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public Optional<User> getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT u FROM User u WHERE u.username = :username";
        User user = entityManager.createQuery(query, User.class)
                .setParameter("username", username)
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    public void updateUserUsername(Integer userId, String newUsername) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setUsername(newUsername);
            entityManager.merge(user);
        }
    }

    public void addUser(User user) {
        entityManager.persist(user);
    }

    public void deleteUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT u FROM User u";
        return entityManager.createQuery(query, User.class).getResultList();
    }

    public List<String> getUserGroups(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return user.getGroups();
        }
        return List.of();
    }

    public List<Group> getGroupsObjForUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<String> groupIds = user.getGroups();
            String query = "SELECT g FROM Group g WHERE g.id IN :groupIds";
            return entityManager.createQuery(query, Group.class)
                    .setParameter("groupIds", groupIds)
                    .getResultList();
        }
        return List.of();
    }

    public void addSongToUser(Integer userId, Integer songId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<String> songs = user.getSongs();
            songs.add(songId.toString());
            user.setSongs(songs);
            entityManager.merge(user);
        }
    }

    public List<String> getUsersSongs(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return user.getSongs();
        }
        return List.of();
    }

    public List<Songs> getSongsObjForUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<String> songIds = user.getSongs();
            String query = "SELECT s FROM Songs s WHERE s.id IN :songIds";
            return entityManager.createQuery(query, Songs.class)
                    .setParameter("songIds", songIds)
                    .getResultList();
        }
        return List.of();
    }

    public void addAwardToUser(Integer userId, String awardId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<String> awards = user.getAwardArray();
            awards.add(awardId);
            user.setAwardArray(awards);
            entityManager.merge(user);
        }
    }

    public List<String> getUserAwards(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return user.getAwardArray();
        }
        return List.of();
    }

    public List<Award> getAwardsObjForUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<String> awardIds = user.getAwardArray();
            String query = "SELECT a FROM Award a WHERE a.id IN :awardIds";
            return entityManager.createQuery(query, Award.class)
                    .setParameter("awardIds", awardIds)
                    .getResultList();
        }
        return List.of();
    }
}