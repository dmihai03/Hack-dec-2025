import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';
import { EventService } from '../../../core/services/event.service';

interface NotificationUser {
  id: number;
  name: string;
  username: string;
}

interface NotificationGroup {
  id: number;
  name: string;
}

interface Notification {
  id: number;
  type: 'STAR_RECEIVED' | 'GROUP_INVITE' | 'SONG_SHARED';
  message: string;
  isRead: boolean;
  createdAt: string;
  fromUser?: NotificationUser;
  relatedGroup?: NotificationGroup;
  // Local state for tracking invite responses
  inviteResponse?: 'accepted' | 'rejected';
}

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notifications.html',
  styleUrls: ['./notifications.css'],
})
export class NotificationsComponent implements OnInit, OnDestroy {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  private authService = inject(AuthService);
  private eventService = inject(EventService);
  private apiUrl = '/api';
  private pollInterval: any;

  notifications: Notification[] = [];
  unreadCount = 0;
  isOpen = false;
  isLoading = false;
  respondedInvites: Map<number, 'accepted' | 'rejected'> = new Map();

  get userId(): number | null {
    return this.authService.userId;
  }

  ngOnInit() {
    if (this.userId) {
      this.loadUnreadCount();
      // Poll for new notifications every 30 seconds
      this.pollInterval = setInterval(() => {
        this.loadUnreadCount();
      }, 30000);
    }
  }

  ngOnDestroy() {
    if (this.pollInterval) {
      clearInterval(this.pollInterval);
    }
  }

  loadUnreadCount() {
    if (!this.userId) return;
    this.http.get<{ count: number }>(`${this.apiUrl}/notifications/user/${this.userId}/count`).subscribe({
      next: (response) => {
        this.unreadCount = response.count;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load notification count', err)
    });
  }

  toggleNotifications() {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.loadNotifications();
    }
  }

  loadNotifications() {
    if (!this.userId) return;
    this.isLoading = true;
    this.http.get<Notification[]>(`${this.apiUrl}/notifications/user/${this.userId}`).subscribe({
      next: (notifications) => {
        this.notifications = notifications;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load notifications', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  markAsRead(notification: Notification) {
    if (notification.isRead) return;
    
    this.http.post(`${this.apiUrl}/notifications/${notification.id}/read`, {}).subscribe({
      next: () => {
        notification.isRead = true;
        this.unreadCount = Math.max(0, this.unreadCount - 1);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to mark as read', err)
    });
  }

  markAllAsRead() {
    if (!this.userId) return;
    this.http.post(`${this.apiUrl}/notifications/user/${this.userId}/read-all`, {}).subscribe({
      next: () => {
        this.notifications.forEach(n => n.isRead = true);
        this.unreadCount = 0;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to mark all as read', err)
    });
  }

  deleteNotification(notification: Notification) {
    this.http.delete(`${this.apiUrl}/notifications/${notification.id}`).subscribe({
      next: () => {
        // Remove from local list
        this.notifications = this.notifications.filter(n => n.id !== notification.id);
        if (!notification.isRead) {
          this.unreadCount = Math.max(0, this.unreadCount - 1);
        }
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to delete notification', err)
    });
  }

  acceptGroupInvite(notification: Notification) {
    if (!notification.relatedGroup || !this.userId) return;
    
    this.http.post(`${this.apiUrl}/groups/${notification.relatedGroup.id}/add`, {
      userID: this.userId
    }).subscribe({
      next: () => {
        // Delete the notification from the database
        this.deleteNotification(notification);
        this.respondedInvites.set(notification.id, 'accepted');
        // Notify other components that user joined a group
        this.eventService.emitGroupJoined();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to join group', err);
        alert('Failed to join group');
      }
    });
  }

  rejectGroupInvite(notification: Notification) {
    // Delete the notification from the database
    this.deleteNotification(notification);
    this.respondedInvites.set(notification.id, 'rejected');
    this.cdr.detectChanges();
  }

  getInviteResponse(notification: Notification): 'accepted' | 'rejected' | null {
    return this.respondedInvites.get(notification.id) || null;
  }

  getNotificationIcon(type: string): string {
    switch (type) {
      case 'STAR_RECEIVED': return '⭐';
      case 'GROUP_INVITE': return '👥';
      case 'SONG_SHARED': return '🎵';
      default: return '🔔';
    }
  }

  formatTime(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'just now';
    if (diffMins < 60) return `${diffMins}m ago`;
    if (diffHours < 24) return `${diffHours}h ago`;
    if (diffDays < 7) return `${diffDays}d ago`;
    return date.toLocaleDateString();
  }
}
