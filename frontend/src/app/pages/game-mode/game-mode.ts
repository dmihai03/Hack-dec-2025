import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameStateService, Avatar } from '../../core/services/game-state.service';
import { Router } from '@angular/router';
import { POSTER_CATALOG } from '../../core/services/poster-catalog';
import { Poster } from '../../core/models/poster';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game-mode.html',
  styleUrls: ['./game-mode.css']
})
export class GameModeComponent implements OnInit {

  showAvatarModal = false;
  avatars: Avatar[] = [];
  selectedAvatar: Avatar | null = null;
  isRetroRoomOpen = false;
  currentPosterIndex = 0;
  isLoading = true;
  posterCatalog: Poster[] = POSTER_CATALOG;

  constructor(
    public gameState: GameStateService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.avatars = this.gameState.avatars;
    this.selectedAvatar = this.gameState.selectedAvatar;

    // Load user data (coins + owned avatars) from backend
    this.gameState.loadUserData().then(() => {
      this.isLoading = false;
      // If no avatar selected, show modal
      if (!this.selectedAvatar) {
        this.showAvatarModal = true;
      }
      this.cdr.detectChanges();
    }).catch(() => {
      this.isLoading = false;
      this.cdr.detectChanges();
    });
  }

  isAvatarOwned(avatar: Avatar): boolean {
    return this.gameState.isAvatarOwned(avatar.id);
  }

  canAffordAvatar(avatar: Avatar): boolean {
    return this.gameState.coins >= avatar.requiredCoins;
  }

  async selectAvatar(avatar: Avatar) {
    // If already owned, just select it
    if (this.gameState.isAvatarOwned(avatar.id)) {
      this.gameState.setSelectedAvatar(avatar);
      this.selectedAvatar = avatar;
      this.showAvatarModal = false;
      this.cdr.detectChanges();
      return;
    }

    // If not owned and costs coins, try to purchase
    if (avatar.requiredCoins > 0) {
      if (!this.canAffordAvatar(avatar)) {
        alert('You need more coins for this artist!');
        return;
      }

      const confirmed = confirm(`Buy ${avatar.name} for 🪙 ${avatar.requiredCoins} coins?`);
      if (!confirmed) return;

      const success = await this.gameState.purchaseAvatar(avatar);
      if (!success) {
        alert('Purchase failed. Please try again.');
        return;
      }
      
      // Force UI update after purchase
      this.cdr.detectChanges();
    }

    this.gameState.setSelectedAvatar(avatar);
    this.selectedAvatar = avatar;
    this.showAvatarModal = false;
    this.cdr.detectChanges();
  }

  openAvatarModal() {
    this.showAvatarModal = true;
  }

  goHome() {
    this.router.navigate(['/']);
  }

  startSinglePlayer() {
    console.log('Start single player with avatar:', this.selectedAvatar);
    this.gameState.setGameMode('single');
    this.router.navigate(['/arena']);
  }

  startMultiPlayer() {
    console.log('Start multiplayer with avatar:', this.selectedAvatar);
    this.gameState.setGameMode('multi');
    this.router.navigate(['/arena']);
  }

  goToRetroRoom() {
    this.router.navigate(['/retro-room']);
  }

  openRetroRoom() {
    this.isRetroRoomOpen = true;
    this.currentPosterIndex = 0;
  }

  closeRetroRoom() {
    this.isRetroRoomOpen = false;
  }

  nextPoster() {
    if (this.currentPosterIndex < this.posterCatalog.length - 1) {
      this.currentPosterIndex++;
    }
  }

  prevPoster() {
    if (this.currentPosterIndex > 0) {
      this.currentPosterIndex--;
    }
  }
  currentPoster(): Poster {
    return this.posterCatalog[this.currentPosterIndex];
  }

  isOwned(poster: Poster): boolean {
    return this.gameState.hasPoster(poster.id);
  }

  async buyPoster(poster: Poster) {
    if (this.gameState.coins < poster.price) {
      alert('Not enough coins!');
      return;
    }

    const confirmed = confirm(`Buy "${poster.name}" for 🪙 ${poster.price} coins?`);
    if (!confirmed) return;

    const success = await this.gameState.purchasePoster(poster);
    if (success) {
      this.cdr.detectChanges();
    } else {
      alert('Failed to purchase poster');
    }
  }
}
