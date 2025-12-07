import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard';
import { GameModeComponent } from './pages/game-mode/game-mode';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './core/guards/auth.guard';
import { ArenaComponent } from './pages/arena/arena';
import { ArenaLyricsComponent } from './pages/arena-lyrics/arena-lyrics';
export const routes: Routes = [
  { path: '', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'game', component: GameModeComponent, canActivate: [authGuard] },
  { path: 'arena', component: ArenaComponent },
  { path: 'arena/lyrics', component: ArenaLyricsComponent }
];
