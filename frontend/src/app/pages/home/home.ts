import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { User } from '../../core/models/user';
import { Song } from '../../core/models/song';
import { Badge } from '../../core/models/badge';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {

  user: User = {
    username: 'musiclover',
    displayName: 'Lavinia',
    rating: 4.8
  };

  songs: Song[] = [
    { title: 'Fix You', artist: 'Coldplay', likes: 14 },
    { title: 'Imagine', artist: 'John Lennon', likes: 20 },
    { title: 'Someone Like You', artist: 'Adele', likes: 9 },
  ];

  badges: Badge[] = [
    { name: 'First Share', description: 'Ai share-uit prima piesă' },
    { name: 'Popular', description: 'Peste 10 like-uri la o piesă' },
  ];
}
