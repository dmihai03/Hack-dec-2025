import { Injectable } from '@angular/core';
import { ArenaQuestion } from '../models/arena-game';

@Injectable({ providedIn: 'root' })
export class ArenaGameService {

  // TEMP: mock data
  getLyricsQuestion(): ArenaQuestion {
    return {
      id: '1',
      lyric: `"Hello, it's me"`,
      correctAnswer: 'Hello',
      reward: 10
    };
  }

  getEmojiQuestion(): ArenaQuestion {
    return {
      id: '2',
      emojis: ['🍾', '💃', '🎶'],
      correctAnswer: 'Uptown Funk',
      reward: 15
    };
  }

  getArtistImageQuestion(): ArenaQuestion {
    return {
      id: '3',
      artist: 'Queen',
      imageUrl: 'assets/queen.jpg',
      correctAnswer: 'Bohemian Rhapsody',
      reward: 20
    };
  }
}
