import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameStateService } from '../../core/services/game-state.service';

@Component({
  selector: 'app-arena-lyrics',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Guess the song from the lyric</h2>

    <p class="lyric">"Hello, it's me"</p>

    <input placeholder="Song title" />
    <button>Submit</button>
  `,
})
export class ArenaLyricsComponent {}
