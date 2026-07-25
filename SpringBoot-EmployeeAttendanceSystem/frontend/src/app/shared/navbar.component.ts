import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatToolbarModule, MatButtonModule, MatIconModule, MatTooltipModule],
  template: `
    <header class="app-header" *ngIf="authService.isLoggedIn()">
      <div class="navbar-content">
        <div class="brand" [routerLink]="authService.getUserRole() === 'ADMIN' ? '/dashboard' : '/attendance'">
          <div class="logo-icon">
            <mat-icon>schedule</mat-icon>
          </div>
          <span class="brand-text">EAMS <span class="brand-sub">Attendance</span></span>
        </div>

        <nav class="nav-links">
          <a mat-button *ngIf="authService.getUserRole() === 'ADMIN'" routerLink="/dashboard" routerLinkActive="active" class="nav-btn">
            <mat-icon>dashboard</mat-icon> Dashboard
          </a>
          <a mat-button routerLink="/attendance" routerLinkActive="active" class="nav-btn">
            <mat-icon>fact_check</mat-icon> Attendance
          </a>
          <a mat-button routerLink="/leave" routerLinkActive="active" class="nav-btn">
            <mat-icon>event_busy</mat-icon> Leave
          </a>
          <a mat-button routerLink="/overtime" routerLinkActive="active" class="nav-btn">
            <mat-icon>more_time</mat-icon> Overtime
          </a>
          <a mat-button *ngIf="authService.getUserRole() === 'ADMIN'" routerLink="/admin" routerLinkActive="active" class="nav-btn admin-btn">
            <mat-icon>admin_panel_settings</mat-icon> Admin Panel
          </a>
        </nav>

        <div class="user-profile">
          <div class="user-badge" [class.admin-badge]="authService.getUserRole() === 'ADMIN'">
            <mat-icon class="badge-icon">account_circle</mat-icon>
            <div class="user-info">
              <span class="user-name">{{ authService.getUsername() || 'User' }}</span>
              <span class="role-tag">{{ authService.getUserRole() || 'EMPLOYEE' }}</span>
            </div>
          </div>
          <button mat-icon-button class="logout-btn" (click)="authService.logout()" matTooltip="Logout">
            <mat-icon>logout</mat-icon>
          </button>
        </div>
      </div>
    </header>
  `,
  styles: [`
    .app-header {
      position: sticky;
      top: 0;
      z-index: 1000;
      background: rgba(255, 255, 255, 0.85);
      backdrop-filter: blur(12px);
      -webkit-backdrop-filter: blur(12px);
      border-bottom: 1px solid rgba(226, 232, 240, 0.8);
      box-shadow: 0 4px 20px -2px rgba(15, 23, 42, 0.05);
    }
    .navbar-content {
      max-width: 1280px;
      margin: 0 auto;
      height: 70px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 1.5rem;
    }
    .brand {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      cursor: pointer;
      text-decoration: none;
    }
    .logo-icon {
      width: 42px;
      height: 42px;
      background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
    }
    .brand-text {
      font-size: 1.35rem;
      font-weight: 800;
      color: #0f172a;
      letter-spacing: -0.02em;
    }
    .brand-sub {
      color: #6366f1;
      font-weight: 600;
    }
    .nav-links {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
    .nav-btn {
      color: #475569 !important;
      font-size: 0.925rem !important;
      border-radius: 10px !important;
      padding: 0.5rem 1rem !important;
      transition: all 0.2s ease !important;
    }
    .nav-btn mat-icon {
      margin-right: 6px;
      font-size: 20px;
      width: 20px;
      height: 20px;
    }
    .nav-btn:hover {
      background: #f1f5f9 !important;
      color: #4f46e5 !important;
    }
    .nav-btn.active {
      background: #e0e7ff !important;
      color: #4338ca !important;
      font-weight: 700 !important;
    }
    .admin-btn {
      color: #0284c7 !important;
    }
    .admin-btn.active {
      background: #e0f2fe !important;
      color: #0369a1 !important;
    }
    .user-profile {
      display: flex;
      align-items: center;
      gap: 1rem;
    }
    .user-badge {
      display: flex;
      align-items: center;
      gap: 0.6rem;
      padding: 0.35rem 0.85rem;
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      border-radius: 9999px;
    }
    .user-badge.admin-badge {
      background: #f0f9ff;
      border-color: #bae6fd;
    }
    .badge-icon {
      color: #6366f1;
    }
    .user-info {
      display: flex;
      flex-direction: column;
      line-height: 1.1;
    }
    .user-name {
      font-size: 0.85rem;
      font-weight: 700;
      color: #1e293b;
    }
    .role-tag {
      font-size: 0.68rem;
      font-weight: 700;
      color: #64748b;
      text-transform: uppercase;
    }
    .logout-btn {
      color: #64748b;
      transition: color 0.2s ease, transform 0.2s ease;
    }
    .logout-btn:hover {
      color: #ef4444;
      transform: scale(1.1);
    }
  `]
})
export class NavbarComponent {
  constructor(public authService: AuthService) {}
}
