import { Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home';
import { RegisterComponent } from './pages/register/register';
import { ProfileComponent } from './pages/profile/profile';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
];
