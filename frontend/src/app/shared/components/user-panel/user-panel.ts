import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { User } from '../../../core/models/user';
import { Song } from '../../../core/models/song';
import { Badge } from '../../../core/models/badge';

interface Group {
  id: number;
  name: string;
}

@Component({
  selector: 'app-user-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-panel.html',
  styleUrls: ['./user-panel.css'],
})
export class UserPanelComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  private apiUrl = '/api';

  // Current user ID (in a real app, this would come from auth service)
  userId = 1;

  user: User | null = null;
  recentSongs: Song[] = [];
  badges: Badge[] = [];
  groups: Group[] = [];

  isLoading = true;
  errorMessage = '';
  showGroups = false;

  ngOnInit() {
    this.loadUserData();
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
}
