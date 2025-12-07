import { Routes } from '@angular/router';

import { DashboardComponent } from './pages/dashboard/dashboard';
import { GameModeComponent } from './pages/game-mode/game-mode';
import { ArenaComponent } from './pages/arena/arena';
import { ArenaEmojiComponent } from './pages/arena-emoji/arena-emoji';
import { ArenaLyricsComponent } from './pages/arena-lyrics/arena-lyrics';
import { ArenaArtistImageComponent } from './pages/arena-artist-image/arena-artist-image';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'game', component: GameModeComponent },

  // Arena hub
  { path: 'arena', component: ArenaComponent },

  // Mini-games
  { path: 'arena/lyrics', component: ArenaLyricsComponent },
  { path: 'arena/emoji', component: ArenaEmojiComponent },
  { path: 'arena/artist-image', component: ArenaArtistImageComponent },
];
