import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { GameStateService } from '../../core/services/game-state.service';

type ArenaGameType = 'lyrics' | 'emoji' | 'artist-image';

@Component({
  selector: 'app-arena',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './arena.html',
  styleUrls: ['./arena.css'],
})
export class ArenaComponent {
  constructor(
    public gameState: GameStateService,
    private router: Router
  ) {}

  startGame(type: ArenaGameType) {
    console.log('Start arena game:', type);
    this.router.navigate(['/arena', type]);
  }

  backToDashboard() {
    this.router.navigate(['/']);
  }
}
