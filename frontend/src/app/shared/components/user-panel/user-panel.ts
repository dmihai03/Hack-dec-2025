import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from '../../../core/models/user';
import { Song } from '../../../core/models/song';
import { Badge } from '../../../core/models/badge';
import { AuthService } from '../../../core/services/auth.service';

interface GroupMember {
  id: number;
  name: string;
  username: string;
}

interface Group {
  id: number;
  name: string;
  members?: GroupMember[];
  sharedSongs?: Song[];
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
  private router = inject(Router);
  private authService = inject(AuthService);
  private apiUrl = '/api';

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

  ngOnInit() {
    if (this.userId) {
      this.loadUserData();
    }
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
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
