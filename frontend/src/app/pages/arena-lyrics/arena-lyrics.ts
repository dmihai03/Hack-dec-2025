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

interface GameQuestion {
  id: number;
  lyric: string;
  options: Song[];
  difficulty: string;
}

interface AnswerResponse {
  correct: boolean;
  correctSong: Song;
}

@Component({
  selector: 'app-arena-lyrics',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './arena-lyrics.html',
  styleUrls: ['./arena-lyrics.css'],
})
export class ArenaLyricsComponent implements OnInit {
  private http = inject(HttpClient);
  private gameState = inject(GameStateService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);
  private apiUrl = '/api';

  question: GameQuestion | null = null;
  selectedSongId: number | null = null;
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
    console.log('Loading question from:', `${this.apiUrl}/game/question`);
    this.http.get<GameQuestion | any>(`${this.apiUrl}/game/question`).subscribe({
      next: (data) => {
        console.log('Received data from backend:', data);
        if (data.error) {
          // No questions available, seed the database
          console.log('No questions available, seeding...');
          this.seedQuestions();
        } else {
          this.question = data;
          this.isLoading = false;
          console.log('Question loaded:', this.question);
          console.log('isLoading:', this.isLoading);
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
    this.http.post(`${this.apiUrl}/game/seed`, {}).subscribe({
      next: (response: any) => {
        console.log('Questions seeded:', response);
        // Now load a question
        this.loadQuestion();
      },
      error: (err) => {
        console.error('Error seeding questions:', err);
        this.feedback = 'Error: Not enough songs in database to create questions.';
        this.isLoading = false;
      }
    });
  }

  selectOption(songId: number) {
    if (this.isAnswered) return;
    this.selectedSongId = songId;
  }

  submit() {
    if (!this.selectedSongId || !this.question || this.isAnswered) return;

    this.http.post<AnswerResponse>(`${this.apiUrl}/game/answer`, {
      questionId: this.question.id,
      answerId: this.selectedSongId
    }).subscribe({
      next: (response) => {
        this.isAnswered = true;
        this.correctAnswer = response.correctSong;
        this.questionsAnswered++;
        
        if (response.correct) {
          this.gameState.addCoins(10);
          this.feedback = '✅ Correct! +10 coins';
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

  resetState() {
    this.selectedSongId = null;
    this.feedback = '';
    this.isAnswered = false;
    this.correctAnswer = null;
  }
}
