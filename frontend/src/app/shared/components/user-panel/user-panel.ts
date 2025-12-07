import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from '../../../core/models/user';
import { Song } from '../../../core/models/song';
import { Badge } from '../../../core/models/badge';
import { AuthService } from '../../../core/services/auth.service';
import { EventService } from '../../../core/services/event.service';
import { NotificationsComponent } from '../notifications/notifications';

interface GroupMember {
  id: number;
  name: string;
  username: string;
}

interface SharedSongSender {
  id: number;
  name: string;
  username: string;
}

interface SongStar {
  id: number;
  voter: {
    id: number;
    name: string;
    username: string;
  };
}

interface SharedSong {
  id: number;
  song: Song;
  sender: SharedSongSender;
  stars?: SongStar[];
  starCount?: number;
}

interface GroupActivity {
  id: number;
  type: 'MEMBER_LEFT' | 'MEMBER_JOINED';
  message: string;
  createdAt: string;
  user: {
    id: number;
    name: string;
    username: string;
  };
}

interface Group {
  id: number;
  name: string;
  members?: GroupMember[];
  sharedSongs?: SharedSong[];
  activities?: GroupActivity[];
}

@Component({
  selector: 'app-user-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, NotificationsComponent],
  templateUrl: './user-panel.html',
  styleUrls: ['./user-panel.css'],
})
export class UserPanelComponent implements OnInit, OnDestroy {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);
  private authService = inject(AuthService);
  private eventService = inject(EventService);
  private apiUrl = '/api';
  private subscriptions: Subscription[] = [];

  // Get user ID from auth service
  get userId(): number | null {
    return this.authService.userId;
  }

  user: User | null = null;
  recentSongs: Song[] = [];
  badges: Badge[] = [];
  groups: Group[] = [];

  isLoading = true;
  errorMessage = '';
  showGroups = false;

  // Popup state
  selectedGroup: Group | null = null;
  isLoadingGroup = false;
  
  // Invite state
  inviteUsername = '';
  inviteStatus = '';
  isInviting = false;

  // Leave group state
  isLeavingGroup = false;
  leaveStatus = '';

  // Create group state
  showCreateGroup = false;
  newGroupName = '';
  isCreatingGroup = false;
  createGroupStatus = '';

  ngOnInit() {
    if (this.userId) {
      this.loadUserData();
    }
    
    // Subscribe to group events
    this.subscriptions.push(
      this.eventService.groupJoined$.subscribe(() => {
        this.loadGroups();
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadUserData() {
    this.isLoading = true;
    this.errorMessage = '';

    // Load user profile
    this.http.get<User>(`${this.apiUrl}/users/${this.userId}/profile`).subscribe({
      next: (user) => {
        this.user = user;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Failed to load user profile';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });

    // Load user songs
    this.http.get<Song[]>(`${this.apiUrl}/users/${this.userId}/songs`).subscribe({
      next: (songs) => {
        this.recentSongs = songs;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load songs', err)
    });

    // Load user badges/awards
    this.http.get<Badge[]>(`${this.apiUrl}/users/${this.userId}/awards`).subscribe({
      next: (badges) => {
        this.badges = badges;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load badges', err)
    });

    // Load user groups
    this.loadGroups();
  }

  loadGroups() {
    if (!this.userId) return;
    
    this.http.get<Group[]>(`${this.apiUrl}/users/${this.userId}/groups`).subscribe({
      next: (groups) => {
        this.groups = groups;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load groups', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleGroups() {
    this.showGroups = !this.showGroups;
  }

  toggleCreateGroup() {
    this.showCreateGroup = !this.showCreateGroup;
    this.createGroupStatus = '';
    if (!this.showCreateGroup) {
      this.newGroupName = '';
    }
  }

  createGroup() {
    if (!this.newGroupName.trim() || !this.userId) return;

    this.isCreatingGroup = true;
    this.createGroupStatus = '';

    this.http.post<{ message: string; groupId: number; groupName: string }>(`${this.apiUrl}/groups/create`, {
      name: this.newGroupName.trim(),
      creatorId: this.userId
    }).subscribe({
      next: (response) => {
        this.createGroupStatus = '✓ Group created!';
        this.isCreatingGroup = false;
        // Add the new group to the list
        this.groups.push({ id: response.groupId, name: response.groupName });
        this.newGroupName = '';
        this.cdr.detectChanges();
        // Hide form after short delay
        setTimeout(() => {
          this.showCreateGroup = false;
          this.createGroupStatus = '';
          this.cdr.detectChanges();
        }, 1500);
      },
      error: (err) => {
        console.error('Failed to create group', err);
        this.createGroupStatus = 'Failed to create group';
        this.isCreatingGroup = false;
        this.cdr.detectChanges();
      }
    });
  }

  openGroupPopup(group: Group) {
    this.isLoadingGroup = true;
    this.selectedGroup = group;

    // Fetch full group details with members and shared songs
    this.http.get<Group>(`${this.apiUrl}/groups/${group.id}`).subscribe({
      next: (fullGroup) => {
        this.selectedGroup = fullGroup;
        this.isLoadingGroup = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load group details', err);
        this.isLoadingGroup = false;
        this.cdr.detectChanges();
      }
    });
  }

  closeGroupPopup() {
    this.selectedGroup = null;
    this.inviteUsername = '';
    this.inviteStatus = '';
    this.leaveStatus = '';
  }

  inviteUser() {
    if (!this.inviteUsername.trim() || !this.selectedGroup) return;
    
    this.isInviting = true;
    this.inviteStatus = '';
    
    // First, find the user by username
    this.http.get<User>(`${this.apiUrl}/users/username/${this.inviteUsername.trim()}`).subscribe({
      next: (invitedUser) => {
        // Now send the invite
        this.http.post<{message?: string, error?: string}>(`${this.apiUrl}/groups/${this.selectedGroup!.id}/invite`, {
          invitedUserId: invitedUser.id,
          inviterId: this.userId
        }).subscribe({
          next: () => {
            this.inviteStatus = `✓ Invite sent to @${this.inviteUsername}`;
            this.inviteUsername = '';
            this.isInviting = false;
            this.cdr.detectChanges();
          },
          error: (err) => {
            this.inviteStatus = err.error?.error || 'Failed to send invite';
            this.isInviting = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        this.inviteStatus = 'User not found';
        this.isInviting = false;
        this.cdr.detectChanges();
      }
    });
  }

  giveStar(sharedSong: SharedSong) {
    this.http.post<{message?: string, error?: string}>(`${this.apiUrl}/groups/shared-songs/${sharedSong.id}/star`, {
      voterId: this.userId
    }).subscribe({
      next: () => {
        // Emit star given event to refresh top users in right panel
        this.eventService.emitStarGiven();
        
        // Refresh group details to get updated star count
        if (this.selectedGroup) {
          this.http.get<Group>(`${this.apiUrl}/groups/${this.selectedGroup.id}`).subscribe({
            next: (fullGroup) => {
              this.selectedGroup = fullGroup;
              this.cdr.detectChanges();
            }
          });
        }
      },
      error: (err) => {
        console.error('Failed to give star', err);
        const errorMsg = err.error?.error || 'Failed to give star';
        alert(errorMsg);
      }
    });
  }

  hasUserStarred(sharedSong: SharedSong): boolean {
    if (!sharedSong.stars) return false;
    return sharedSong.stars.some(star => star.voter.id === this.userId);
  }

  canGiveStar(sharedSong: SharedSong): boolean {
    // Can't star your own song
    if (sharedSong.sender.id === this.userId) return false;
    // Can't star if already starred
    if (this.hasUserStarred(sharedSong)) return false;
    return true;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  leaveGroup() {
    if (!this.selectedGroup || !this.userId) return;
    
    this.isLeavingGroup = true;
    this.leaveStatus = '';
    
    this.http.post(`${this.apiUrl}/groups/${this.selectedGroup.id}/remove`, {
      userID: this.userId
    }).subscribe({
      next: () => {
        this.leaveStatus = '✓ You left the group';
        this.isLeavingGroup = false;
        // Remove from local groups list
        this.groups = this.groups.filter(g => g.id !== this.selectedGroup?.id);
        this.cdr.detectChanges();
        // Close popup after a short delay
        setTimeout(() => {
          this.closeGroupPopup();
        }, 1500);
      },
      error: (err) => {
        console.error('Failed to leave group', err);
        this.leaveStatus = 'Failed to leave group';
        this.isLeavingGroup = false;
        this.cdr.detectChanges();
      }
    });
  }

  formatActivityTime(dateString: string): string {
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
