import { Component, OnInit, inject, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';

interface ReceivedSong {
  from: string;
  title: string;
  artist: string;
}

interface Group {
  id: number;
  name: string;
}

interface TopUser {
  id: number;
  name: string;
  username: string;
  ratingNumber: number;
}

@Component({
  selector: 'app-right-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './right-panel.html',
  styleUrls: ['./right-panel.css']
})
export class RightPanelComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  private zone = inject(NgZone);
  private authService = inject(AuthService);
  private apiUrl = '/api';

  // Get user ID from auth service
  get userId(): number | null {
    return this.authService.userId;
  }

  // Form fields
  songName = '';
  selectedGroupId: number | null = null;

  // User's groups
  groups: Group[] = [];

  // Status message
  statusMessage = '';
  isError = false;
  isSending = false;

  // Music box songs
  receivedSongs: ReceivedSong[] = [];
  // Top users by rating
  topUsers: TopUser[] = [];

  ngOnInit() {
    if (this.userId) {
      this.loadUserGroups();
    }
  }

  loadUserGroups() {
    this.http.get<Group[]>(`${this.apiUrl}/users/${this.userId}/groups`).subscribe({
      next: (groups) => {
        this.groups = groups;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load groups', err)
    });
  }

  loadTopUsers() {
    this.http.get<TopUser[]>(`${this.apiUrl}/users/top/3`).subscribe({
      next: (users) => {
        this.topUsers = users;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load top users', err)
    });
  }

  sendSong() {
    // Clear previous status
    this.statusMessage = '';
    this.isError = false;

    // Validation
    if (!this.songName.trim()) {
      this.statusMessage = 'Please enter a song name';
      this.isError = true;
      this.cdr.detectChanges();
      return;
    }

    if (!this.selectedGroupId) {
      this.statusMessage = 'Please select a group';
      this.isError = true;
      this.cdr.detectChanges();
      return;
    }

    this.isSending = true;
    this.cdr.detectChanges();

    // Send song to group with sender ID
    this.http.post(`${this.apiUrl}/groups/${this.selectedGroupId}/share-song`, {
      songTitle: this.songName.trim(),
      senderId: this.userId
    }, { responseType: 'text' }).subscribe({
      next: (response) => {
        this.zone.run(() => {
          this.statusMessage = '🎵 Song shared successfully!';
          this.isError = false;
          this.isSending = false;
          this.songName = '';
          this.selectedGroupId = null;
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        this.zone.run(() => {
          this.statusMessage = err.error || 'Failed to share song';
          this.isError = true;
          this.isSending = false;
          this.cdr.detectChanges();
        });
      }
    });
  }
}
