import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Poster } from '../models/poster';
import { AuthService } from './auth.service';
import { firstValueFrom } from 'rxjs';

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
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = '/api';

  private _coins = 0;
  private _ownedAvatars: Set<string> = new Set(['default']);
  private _selectedAvatar: Avatar | null = null;
  private _posters: Poster[] = [];
  private _isLoaded = false;
  gameMode: GameModeType | null = null;

  constructor() {
    // ✅ Poster default – garantat inițializat
    this._posters.push({
      id: 'aerosmith-rock',
      name: 'Aerosmith – Rock',
      imageUrl: 'assets/posters/aerosmith-rock.png',
      price: 0
    });

    console.log('GameState posters:', this._posters);
  }

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
      imageUrl: 'assets/avatars/b-eilish.png',
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

  get isLoaded() {
    return this._isLoaded;
  }

  setCoins(amount: number) {
    this._coins = amount;
  }

  isAvatarOwned(avatarId: string): boolean {
    return this._ownedAvatars.has(avatarId);
  }

  async loadUserData(): Promise<void> {
    const userId = this.authService.userId;
    if (!userId) {
      this._isLoaded = true;
      return;
    }

    try {
      // Load coins
      const coinsResponse = await firstValueFrom(
        this.http.get<{ coins: number }>(`${this.apiUrl}/users/${userId}/coins`)
      );
      this._coins = coinsResponse.coins ?? 0;

      // Load owned avatars
      try {
        const avatarsResponse = await firstValueFrom(
          this.http.get<{ ownedAvatars: string }>(`${this.apiUrl}/users/${userId}/avatars`)
        );
        if (avatarsResponse.ownedAvatars) {
          const ownedList = avatarsResponse.ownedAvatars.split(',').filter(id => id.trim());
          this._ownedAvatars = new Set(ownedList);
        }
      } catch (avatarErr) {
        console.error('Failed to load avatars, using default:', avatarErr);
        this._ownedAvatars = new Set(['default']);
      }

      this._isLoaded = true;
    } catch (err) {
      console.error('Failed to load user data:', err);
      this._isLoaded = true; // Still mark as loaded to remove spinner
    }
  }

  loadCoinsFromBackend(): void {
    this.loadUserData();
  }

  addCoins(amount: number): void {
    const userId = this.authService.userId;
    if (userId) {
      this.http.post<{ coins: number }>(`${this.apiUrl}/users/${userId}/coins/add`, { amount }).subscribe({
        next: (response) => {
          this._coins = response.coins;
        },
        error: (err) => {
          console.error('Failed to add coins:', err);
        }
      });
    } else {
      this._coins += amount;
    }
  }

  async purchaseAvatar(avatar: Avatar): Promise<boolean> {
    const userId = this.authService.userId;
    if (!userId) return false;

    // Already owned
    if (this._ownedAvatars.has(avatar.id)) {
      return true;
    }

    // Not enough coins
    if (this._coins < avatar.requiredCoins) {
      return false;
    }

    try {
      const response = await firstValueFrom(
        this.http.post<{ success: boolean; coins: number; ownedAvatars: string }>(
          `${this.apiUrl}/users/${userId}/avatars/purchase`,
          { avatarId: avatar.id, cost: avatar.requiredCoins }
        )
      );

      if (response.success) {
        this._coins = response.coins;
        const ownedList = response.ownedAvatars.split(',').filter(id => id.trim());
        this._ownedAvatars = new Set(ownedList);
        return true;
      }
    } catch (err) {
      console.error('Failed to purchase avatar:', err);
    }
    return false;
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
    // mică protecție să nu îl adăugăm de 2 ori
    if (!this._posters.some(p => p.id === poster.id)) {
      this._posters.push(poster);
    }
  }

  hasPoster(id: string): boolean {
    return this._posters.some(p => p.id === id);
  }
  
}

