import { Component, OnInit, inject, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface ReceivedSong {
  from: string;
  title: string;
  artist: string;
}

interface Group {
  id: number;
  name: string;
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
  private apiUrl = '/api';

  // Current user ID (should come from auth service)
  userId = 1;

  // Form fields
  songName = '';
  selectedGroupId: number | null = null;

  // User's groups
  groups: Group[] = [];

  // Status message
  statusMessage = '';
  isError = false;
  isSending = false;

  // MOCK – vine din backend mai târziu
  receivedSongs: ReceivedSong[] = [
    { from: 'alex_rock', title: 'Numb', artist: 'Linkin Park' },
    { from: 'maria_vibes', title: 'Summertime Sadness', artist: 'Lana Del Rey' },
    { from: 'ionut', title: 'Bohemian Rhapsody', artist: 'Queen' }
  ];

  ngOnInit() {
    this.loadUserGroups();
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

  openReceivedSong(song: ReceivedSong) {
    console.log('Clicked song:', song);
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

    // Send song to group
    this.http.post(`${this.apiUrl}/groups/${this.selectedGroupId}/share-song`, {
      songTitle: this.songName.trim()
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
