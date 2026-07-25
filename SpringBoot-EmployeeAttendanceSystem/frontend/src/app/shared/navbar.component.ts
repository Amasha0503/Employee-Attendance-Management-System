import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatToolbarModule, MatButtonModule, MatIconModule],
  template: `
    <mat-toolbar color="primary" class="navbar" *ngIf="authService.isLoggedIn()">
      <span class="brand" routerLink="/dashboard">
        <mat-icon>assessment</mat-icon> EAMS
      </span>
      <span class="spacer"></span>
      <a mat-button routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
      <a mat-button routerLink="/attendance" routerLinkActive="active">Attendance</a>
      <a mat-button routerLink="/leave" routerLinkActive="active">Leave</a>
      <a mat-button routerLink="/overtime" routerLinkActive="active">Overtime</a>
      <a mat-button *ngIf="authService.getUserRole() === 'ADMIN'" routerLink="/admin" routerLinkActive="active">Admin Panel</a>
      <button mat-icon-button (click)="authService.logout()" title="Logout">
        <mat-icon>exit_to_app</mat-icon>
      </button>
    </mat-toolbar>
  `,
  styles: [`
    .navbar {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
    .brand {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-weight: bold;
      font-size: 1.25rem;
      cursor: pointer;
    }
    .spacer {
      flex: 1 1 auto;
    }
    .active {
      background-color: rgba(255, 255, 255, 0.15);
    }
  `]
})
export class NavbarComponent {
  constructor(public authService: AuthService) {}
}
