import { Injectable } from '@angular/core';
import { Poster } from '../models/poster';

export type GameModeType = 'single' | 'multi';

export interface Avatar {
  id: string;
  name: string;
  imageUrl: string;
  requiredCoins: number;
}

@Injectable({
  providedIn: 'root'
})
export class GameStateService {

  private _coins = 0;
  private _selectedAvatar: Avatar | null = null;
  private _posters: Poster[] = [];
  gameMode: GameModeType | null = null;

  private _avatars: Avatar[] = [
    {
      id: 'default',
      name: 'Unknown Artist',
      imageUrl: 'assets/avatars/default.png',
      requiredCoins: 0
    },
    {
      id: 'billie',
      name: 'Billie Eilish',
      imageUrl: 'assets/avatars/billie.png',
      requiredCoins: 50
    },
    {
      id: 'weeknd',
      name: 'The Weeknd',
      imageUrl: 'assets/avatars/weeknd.png',
      requiredCoins: 100
    }
  ];

  get coins() {
    return this._coins;
  }

  addCoins(amount: number) {
    this._coins += amount;
  }

  get avatars() {
    return this._avatars;
  }

  get selectedAvatar() {
    return this._selectedAvatar;
  }

  setSelectedAvatar(avatar: Avatar) {
    this._selectedAvatar = avatar;
  }

  setGameMode(mode: GameModeType) {
    this.gameMode = mode;
  }

  get posters(): Poster[] {
    return this._posters;
  }

  addPoster(poster: Poster) {
    if (!this.hasPoster(poster.id)) {
      this._posters.push(poster);
    }
  }

  hasPoster(id: string): boolean {
    return this._posters.some(p => p.id === id);
  }
}
