import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface ReceivedSong {
  from: string;
  title: string;
  artist: string;
}

@Component({
  selector: 'app-right-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './right-panel.html',
  styleUrls: ['./right-panel.css']
})
export class RightPanelComponent {

  // MOCK – vine din backend mai târziu
  receivedSongs: ReceivedSong[] = [
    { from: 'alex_rock', title: 'Numb', artist: 'Linkin Park' },
    { from: 'maria_vibes', title: 'Summertime Sadness', artist: 'Lana Del Rey' },
    { from: 'ionut', title: 'Bohemian Rhapsody', artist: 'Queen' }
  ];

  openReceivedSong(song: ReceivedSong) {
  console.log('Clicked song:', song);
}

  sendSong() {
    alert('Song sent (mock)');
  }
}
