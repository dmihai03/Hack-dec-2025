import { Component, inject, PLATFORM_ID, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

export interface Song {
  id: number;
  title: string;
  artist: string;
  category: string;
}

interface TrendingSong {
  id: number;
  title: string;
  artist: string;
  category: string;
  likes: number;
}

@Component({
  selector: 'app-main-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './main-panel.html',
  styleUrls: ['./main-panel.css']
})
export class MainPanelComponent implements OnInit {
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

  trendingSongs: TrendingSong[] = [];

  trendingCategories = ['Rock', 'Pop', 'Hip-Hop', 'Folk'];
  selectedTrendingCategory: string | null = null;

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadTrendingSongs();
    }
  }

  loadTrendingSongs() {
    this.http.get<TrendingSong[]>(`${this.apiUrl}/songs/trending`).subscribe({
      next: (songs) => {
        this.trendingSongs = songs;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load trending songs:', err);
      }
    });
  }

  get filteredTrendingSongs(): TrendingSong[] {
    let songs = this.trendingSongs;
    if (this.selectedTrendingCategory) {
      songs = songs.filter(s => s.category === this.selectedTrendingCategory);
    }
    return songs.sort((a, b) => (b.likes || 0) - (a.likes || 0)).slice(0, 5);
  }

  setTrendingCategory(category: string | null) {
    if (this.selectedTrendingCategory === category) {
      this.selectedTrendingCategory = null;
    } else {
      this.selectedTrendingCategory = category;
    }
  }

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

    this.http.post<Song | Song[]>(`${this.apiUrl}/songs/search/${this.activeFilter}`, {
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

  constructor(private router: Router) {}
  enterGameMode() {
    console.log('ENTER GAME MODE');
    this.router.navigate(['/game']);
  }
}
