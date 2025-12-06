import { Component, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

export interface Song {
  id: number;
  title: string;
  artist: string;
  category: string;
}

interface TrendingSong {
  title: string;
  artist: string;
  likes: number;
}

@Component({
  selector: 'app-main-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './main-panel.html',
  styleUrls: ['./main-panel.css']
})
export class MainPanelComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);
  private apiUrl = '/api';

  searchQuery = '';
  activeFilter: 'title' | 'artist' | 'category' = 'title';
  searchResults: Song[] = [];
  isLoading = false;
  errorMessage = '';
  hasSearched = false;

  trendingSongs: TrendingSong[] = [
    { title: 'Fix You', artist: 'Coldplay', likes: 120 },
    { title: 'Imagine', artist: 'John Lennon', likes: 98 },
    { title: "Sweet Child O' Mine", artist: "Guns N' Roses", likes: 87 }
  ];

  setFilter(filter: 'title' | 'artist' | 'category') {
    this.activeFilter = filter;
  }

  setQuery(query: string) {
    this.searchQuery = query;
  }

  search() {
    if (!this.searchQuery.trim()) {
      console.log('Empty query, skipping search');
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.searchResults = [];
    this.hasSearched = true;

    this.http.post<Song | Song[]>(`${this.apiUrl}/songs/${this.activeFilter}`, {
      query: this.searchQuery
    }).subscribe({
      next: (response) => {
        // Handle both single result (title) and array results (artist/category)
        this.searchResults = Array.isArray(response) ? response : (response ? [response] : []);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Failed to fetch results. Check if backend is running.';
        this.searchResults = [];
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  enterGameMode() {
    console.log('ENTER GAME MODE');
    this.router.navigate(['/game']);
    // TODO: route to /game
  }
}
