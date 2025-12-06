import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard';
import { GameModeComponent } from './pages/game-mode/game-mode';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'game', component: GameModeComponent },
];
