package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.Song;
import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Entities.Award;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public Optional<User> getUserById(Integer id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT u FROM User u WHERE u.username = :username";
        List<User> results = entityManager.createQuery(query, User.class)
                .setParameter("username", username)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<User> getUserByEmail(String email) {
        String query = "SELECT u FROM User u WHERE u.email = :email";
        List<User> results = entityManager.createQuery(query, User.class)
                .setParameter("email", email)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
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

    public List<Group> getUserGroups(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return user.getGroups();
        }
        return List.of();
    }

    public void addGroupToUser(Integer userId, Integer groupId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<Group> groups = user.getGroups();
            if (groups.stream().anyMatch(g -> g.getId().equals(groupId))) {
                return; // Group already added
            }

            groups.add(entityManager.find(Group.class, groupId));
            user.setGroups(groups);
            entityManager.merge(user);
        }
    }

    public List<Song> getUsersSongs(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return user.getSongs();
        }
        return List.of();
    }

    public void addSongToUser(Integer userId, Integer songId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<Song> songs = user.getSongs();
            songs.add(entityManager.find(Song.class, songId));
            user.setSongs(songs);
            entityManager.merge(user);
        }
    }

    public List<Award> getUserAwards(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return user.getAwards();
        }
        return List.of();
    }

    public void addAwardToUser(Integer userId, Integer awardId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            List<Award> awards = user.getAwards();
            awards.add(entityManager.find(Award.class, awardId));
            user.setAwards(awards);
            entityManager.merge(user);
        }
    }

    public void removeGroupFromUser(Integer userID, Integer groupID) {
        User user = entityManager.find(User.class, userID);
        if (user != null) {
            List<Group> groups = user.getGroups();
            groups.removeIf(g -> g.getId().equals(groupID));
            user.setGroups(groups);
            entityManager.merge(user);
        }
    }

    public void removeSongFromUser(Integer userID, Integer songID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeSongFromUser'");
    }
}
