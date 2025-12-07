import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ArenaGameService } from '../../core/services/arena-game';
import { GameStateService } from '../../core/services/game-state.service';
import { ArenaQuestion } from '../../core/models/arena-game';

@Component({
  selector: 'app-arena-artist-image',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './arena-artist-image.html',
  styleUrls: ['./arena-artist-image.css'],
})
export class ArenaArtistImageComponent implements OnInit {

  question!: ArenaQuestion;
  answer = '';
  feedback = '';

  constructor(
    private arenaGame: ArenaGameService,
    private gameState: GameStateService
  ) {}

  ngOnInit(): void {
    this.question = this.arenaGame.getArtistImageQuestion();
  }

  submit() {
    if (
      this.answer.toLowerCase().trim() ===
      this.question.correctAnswer.toLowerCase()
    ) {
      this.gameState.addCoins(this.question.reward);
      this.feedback = '✅ Correct!';
    } else {
      this.feedback = '❌ Try again';
    }
  }
}
