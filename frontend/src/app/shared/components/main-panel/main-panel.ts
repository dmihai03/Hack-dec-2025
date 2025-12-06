import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // ✅ ADD
import { Router } from '@angular/router';

interface TrendingSong {
  title: string;
  artist: string;
  likes: number;
}

@Component({
  selector: 'app-main-panel',
  standalone: true,
  imports: [CommonModule, FormsModule], // ✅ ADD
  templateUrl: './main-panel.html',
  styleUrls: ['./main-panel.css']
})

export class MainPanelComponent {

  searchQuery = '';
  activeFilter: 'song' | 'artist' | 'genre' = 'song';

  trendingSongs: TrendingSong[] = [
    { title: 'Fix You', artist: 'Coldplay', likes: 120 },
    { title: 'Imagine', artist: 'John Lennon', likes: 98 },
    { title: 'Sweet Child O’ Mine', artist: 'Guns N’ Roses', likes: 87 }
  ];

  setFilter(filter: 'song' | 'artist' | 'genre') {
    this.activeFilter = filter;
  }

  search() {
    console.log('Searching:', this.searchQuery, 'filter:', this.activeFilter);
    // TODO: backend request
  }

  constructor(private router: Router) {}
  enterGameMode() {
    console.log('ENTER GAME MODE');
    this.router.navigate(['/game']);
    // TODO: route to /game
  }
}
