import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
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
export class UserPanelComponent {
  user: User = {
    username: 'lavinia_music',
    displayName: 'Lavinia',
    rating: 42,
  };

  recentSongs: Song[] = [
    { title: 'Fix You', artist: 'Coldplay', likes: 14 },
    { title: 'Imagine', artist: 'John Lennon', likes: 20 },
  ];

  badges: Badge[] = [
    { name: 'First Share', description: 'Prima piesă share-uită' },
    { name: 'Daily Star', description: 'Top rating într-o zi' },
  ];

  groups: Group[] = [
    { id: 1, name: 'Rock Lovers' },
    { id: 2, name: 'Chill Vibes' },
  ];

  showGroups = false;

  toggleGroups() {
    this.showGroups = !this.showGroups;
  }
}
