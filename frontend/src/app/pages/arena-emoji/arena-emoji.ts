import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { GameStateService } from '../../core/services/game-state.service';

interface Song {
  id: number;
  title: string;
  artist: string;
}

interface EmojiQuestion {
  id: number;
  emojis: string;
  difficulty: string;
}

interface AnswerResponse {
  correct: boolean;
  correctSong: Song;
}

@Component({
  selector: 'app-arena-emoji',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './arena-emoji.html',
  styleUrls: ['./arena-emoji.css'],
})
export class ArenaEmojiComponent implements OnInit {
  private http = inject(HttpClient);
  private gameState = inject(GameStateService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);
  private apiUrl = '/api';

  question: EmojiQuestion | null = null;
  answer = '';
  feedback = '';
  isAnswered = false;
  isLoading = true;
  correctAnswer: Song | null = null;
  questionsAnswered = 0;
  maxQuestions = 3;

  ngOnInit(): void {
    this.loadQuestion();
  }

  loadQuestion() {
    this.isLoading = true;
    this.http.get<EmojiQuestion | any>(`${this.apiUrl}/emoji-game/question`).subscribe({
      next: (data) => {
        if (data.error) {
          console.log('No questions available, seeding...');
          this.seedQuestions();
        } else {
          this.question = data;
          this.isLoading = false;
          this.resetState();
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        console.error('Failed to load question', err);
        this.feedback = 'Failed to load question. Please try again.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  seedQuestions() {
    this.http.post(`${this.apiUrl}/emoji-game/seed`, {}).subscribe({
      next: (response: any) => {
        console.log('Questions seeded:', response);
        this.loadQuestion();
      },
      error: (err) => {
        console.error('Error seeding questions:', err);
        this.feedback = 'Error: Not enough songs in database.';
        this.isLoading = false;
      }
    });
  }

  submit() {
    if (!this.answer.trim() || !this.question || this.isAnswered) return;

    this.http.post<AnswerResponse>(`${this.apiUrl}/emoji-game/answer`, {
      questionId: this.question.id,
      answer: this.answer
    }).subscribe({
      next: (response) => {
        this.isAnswered = true;
        this.correctAnswer = response.correctSong;
        this.questionsAnswered++;
        
        if (response.correct) {
          this.gameState.addCoins(15);
          this.feedback = '✅ Correct! +15 coins';
        } else {
          this.feedback = `❌ Wrong! The correct answer was "${response.correctSong.title}" by ${response.correctSong.artist}`;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to check answer', err);
        this.feedback = 'Failed to check answer. Please try again.';
      }
    });
  }

  nextQuestion() {
    this.loadQuestion();
  }

  backToArena() {
    this.router.navigate(['/arena']);
  }

  get isLastQuestion(): boolean {
    return this.questionsAnswered >= this.maxQuestions;
  }

  // Convert emoji string to array for display
  get emojiArray(): string[] {
    if (!this.question?.emojis) return [];
    // Split emojis - handles multi-byte emoji characters
    return [...this.question.emojis];
  }

  resetState() {
    this.answer = '';
    this.feedback = '';
    this.isAnswered = false;
    this.correctAnswer = null;
  }
}
