import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserPanelComponent } from '../../shared/components/user-panel/user-panel';
import { MainPanelComponent } from '../../shared/components/main-panel/main-panel';
import { RightPanelComponent } from '../../shared/components/right-panel/right-panel';

@Component({
  standalone: true,
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    UserPanelComponent,
    MainPanelComponent,
    RightPanelComponent
  ],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent {}
