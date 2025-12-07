import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArenaGameService } from '../../core/services/arena-game';
import { GameStateService } from '../../core/services/game-state.service';
import { ArenaQuestion } from '../../core/models/arena-game';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-arena-emoji',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './arena-emoji.html',
  styleUrls: ['./arena-emoji.css'],
})
export class ArenaEmojiComponent implements OnInit {

  question!: ArenaQuestion;
  answer = '';
  feedback = '';

  constructor(
    private arenaGame: ArenaGameService,
    private gameState: GameStateService
  ) {}

  ngOnInit(): void {
    this.question = this.arenaGame.getEmojiQuestion();
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
