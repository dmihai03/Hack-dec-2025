import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameStateService, Avatar } from '../../core/services/game-state.service';
import { Router } from '@angular/router';

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
  coins = 0;
  selectedAvatar: Avatar | null = null;

  constructor(
  private gameState: GameStateService,
  private router: Router
) {}


  ngOnInit(): void {
    this.avatars = this.gameState.avatars;
    this.coins = this.gameState.coins;
    this.selectedAvatar = this.gameState.selectedAvatar;

    // dacă nu are niciun avatar selectat, forțează popup-ul
    if (!this.selectedAvatar) {
      this.showAvatarModal = true;
    }
  }

  canUseAvatar(avatar: Avatar): boolean {
    return this.coins >= avatar.requiredCoins;
  }

  selectAvatar(avatar: Avatar) {
    // pentru default nu verificăm coins
    if (avatar.requiredCoins > 0 && this.coins < avatar.requiredCoins) {
      // eventual pui un mesaj de eroare
      alert('You need more coins for this artist!');
      return;
    }

    this.gameState.setSelectedAvatar(avatar);
    this.selectedAvatar = avatar;
    this.showAvatarModal = false;
  }

  openAvatarModal() {
    this.showAvatarModal = true;
  }

  // pentru mai târziu – aici o să ducem userul în arena
  startSinglePlayer() {
    console.log('Start single player with avatar:', this.selectedAvatar);
    this.gameState.setGameMode('single'); // dacă ai metoda
    this.router.navigate(['/arena']);
  }

  startMultiPlayer() {
    console.log('Start multiplayer with avatar:', this.selectedAvatar);
    this.gameState.setGameMode('multi');
    this.router.navigate(['/arena']);
  }
}
