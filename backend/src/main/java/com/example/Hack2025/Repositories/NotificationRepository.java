package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Group;
import com.example.Hack2025.Entities.Notification;
import com.example.Hack2025.Entities.Notification.NotificationType;
import com.example.Hack2025.Entities.User;

import java.util.List;

@Repository
@Transactional
public class NotificationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Notification> getNotificationsByUserId(Integer userId) {
        String query = "SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC";
        return entityManager.createQuery(query, Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Notification> getUnreadNotificationsByUserId(Integer userId) {
        String query = "SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC";
        return entityManager.createQuery(query, Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public int getUnreadCount(Integer userId) {
        String query = "SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return count.intValue();
    }

    public void createNotification(User user, NotificationType type, String message, User fromUser, Group relatedGroup) {
        Notification notification = new Notification(user, type, message, fromUser);
        notification.setRelatedGroup(relatedGroup);
        entityManager.persist(notification);
    }

    public void createStarNotification(User recipient, User fromUser, String songTitle) {
        String message = "@" + fromUser.getUsername() + " gave you a ⭐ for sharing \"" + songTitle + "\"";
        Notification notification = new Notification(recipient, NotificationType.STAR_RECEIVED, message, fromUser);
        entityManager.persist(notification);
    }

    public void createGroupInviteNotification(User recipient, User fromUser, Group group) {
        String message = "@" + fromUser.getUsername() + " invited you to join \"" + group.getName() + "\"";
        Notification notification = new Notification(recipient, NotificationType.GROUP_INVITE, message, fromUser);
        notification.setRelatedGroup(group);
        entityManager.persist(notification);
    }

    public void createSongSharedNotification(User recipient, User fromUser, Group group, String songTitle) {
        String message = "@" + fromUser.getUsername() + " shared \"" + songTitle + "\" in " + group.getName();
        Notification notification = new Notification(recipient, NotificationType.SONG_SHARED, message, fromUser);
        notification.setRelatedGroup(group);
        entityManager.persist(notification);
    }

    public void markAsRead(Integer notificationId) {
        Notification notification = entityManager.find(Notification.class, notificationId);
        if (notification != null) {
            notification.setIsRead(true);
            entityManager.merge(notification);
        }
    }

    public void markAllAsRead(Integer userId) {
        String query = "UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false";
        entityManager.createQuery(query)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public void deleteNotification(Integer notificationId) {
        Notification notification = entityManager.find(Notification.class, notificationId);
        if (notification != null) {
            entityManager.remove(notification);
        }
    }
}
