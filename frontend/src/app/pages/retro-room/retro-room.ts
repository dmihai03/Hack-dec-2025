import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameStateService } from '../../core/services/game-state.service';
import { Poster } from '../../core/models/poster';

@Component({
  selector: 'app-retro-room',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './retro-room.html',
  styleUrls: ['./retro-room.css'],
})
export class RetroRoomComponent {

  marketPosters: Poster[] = [
    {
      id: 'queen',
      name: 'Queen – Bohemian Rhapsody',
      imageUrl: 'assets/posters/queen.jpg',
      price: 50
    },
    {
      id: 'coldplay',
      name: 'Coldplay – Viva la Vida',
      imageUrl: 'assets/posters/coldplay.jpg',
      price: 40
    }
  ];

  constructor(public gameState: GameStateService) {}

  async buyPoster(poster: Poster) {
    if (this.gameState.coins < poster.price) {
      alert('Not enough coins');
      return;
    }

    if (this.gameState.hasPoster(poster.id)) {
      alert('Already owned');
      return;
    }

    const success = await this.gameState.purchasePoster(poster);
    if (!success) {
      alert('Failed to purchase poster');
    }
  }
}
