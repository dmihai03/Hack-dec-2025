import { Routes } from '@angular/router';

import { DashboardComponent } from './pages/dashboard/dashboard';
import { GameModeComponent } from './pages/game-mode/game-mode';
import { ArenaComponent } from './pages/arena/arena';
import { ArenaEmojiComponent } from './pages/arena-emoji/arena-emoji';
import { ArenaLyricsComponent } from './pages/arena-lyrics/arena-lyrics';
import { ArenaArtistImageComponent } from './pages/arena-artist-image/arena-artist-image';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'game', component: GameModeComponent, canActivate: [authGuard] },

  // Arena hub
  { path: 'arena', component: ArenaComponent, canActivate: [authGuard] },

  // Mini-games
  { path: 'arena/lyrics', component: ArenaLyricsComponent, canActivate: [authGuard] },
  { path: 'arena/emoji', component: ArenaEmojiComponent, canActivate: [authGuard] },
  { path: 'arena/artist-image', component: ArenaArtistImageComponent, canActivate: [authGuard] },
];
