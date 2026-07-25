import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <div class="home-portal">
      <div class="hero-banner">
        <span class="pill-tag">Enterprise Attendance & HR Suite</span>
        <h1>Streamlined Employee Attendance Management</h1>
        <p class="hero-desc">
          Welcome, {{ authService.getUsername() || 'Employee' }}! Access your attendance check-ins, request leaves and overtime, and monitor real-time department analytics.
        </p>
        <div class="hero-actions">
          <button *ngIf="authService.getUserRole() === 'ADMIN'" mat-raised-button color="primary" routerLink="/dashboard">
            <mat-icon>dashboard</mat-icon> Open Dashboard
          </button>
          <button mat-stroked-button routerLink="/attendance">
            <mat-icon>fact_check</mat-icon> Check-In Now
          </button>
        </div>
      </div>

      <div class="features-grid">
        <mat-card class="portal-card" routerLink="/attendance">
          <div class="card-icon indigo">
            <mat-icon>alarm_on</mat-icon>
          </div>
          <h3>Attendance Check-In</h3>
          <p>Mark daily check-in and check-out times with automated late and early arrival tracking.</p>
          <span class="card-link">Launch Module &rarr;</span>
        </mat-card>

        <mat-card class="portal-card" routerLink="/leave">
          <div class="card-icon amber">
            <mat-icon>event_busy</mat-icon>
          </div>
          <h3>Leave Requests</h3>
          <p>Apply for annual, sick, or casual leave and check your real-time leave balance.</p>
          <span class="card-link">Launch Module &rarr;</span>
        </mat-card>

        <mat-card class="portal-card" routerLink="/overtime">
          <div class="card-icon teal">
            <mat-icon>more_time</mat-icon>
          </div>
          <h3>Overtime Tracker</h3>
          <p>Submit overtime logs for approval and monitor monthly approved overtime hours.</p>
          <span class="card-link">Launch Module &rarr;</span>
        </mat-card>

        <mat-card *ngIf="authService.getUserRole() === 'ADMIN'" class="portal-card" routerLink="/dashboard">
          <div class="card-icon purple">
            <mat-icon>analytics</mat-icon>
          </div>
          <h3>Analytics & Reports</h3>
          <p>View interactive attendance trend charts and export CSV or PDF reports.</p>
          <span class="card-link">Launch Module &rarr;</span>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .home-portal {
      max-width: 1200px;
      margin: 0 auto;
      padding: 3rem 1.5rem;
    }
    .hero-banner {
      background: linear-gradient(135deg, #1e1b4b 0%, #312e81 100%);
      color: white;
      padding: 3.5rem 2.5rem;
      border-radius: 24px;
      box-shadow: 0 20px 40px -15px rgba(30, 27, 75, 0.4);
      margin-bottom: 3rem;
    }
    .pill-tag {
      display: inline-block;
      padding: 0.35rem 0.85rem;
      background: rgba(255, 255, 255, 0.15);
      border-radius: 9999px;
      font-size: 0.75rem;
      font-weight: 700;
      letter-spacing: 0.05em;
      text-transform: uppercase;
      margin-bottom: 1.25rem;
      backdrop-filter: blur(8px);
    }
    h1 {
      font-size: clamp(2rem, 4vw, 3rem);
      font-weight: 800;
      margin: 0 0 1rem;
      letter-spacing: -0.02em;
      line-height: 1.15;
    }
    .hero-desc {
      font-size: 1.1rem;
      color: #c7d2fe;
      max-width: 650px;
      margin: 0 0 2rem;
      line-height: 1.6;
    }
    .hero-actions {
      display: flex;
      gap: 1rem;
      flex-wrap: wrap;
    }
    .hero-actions button {
      height: 48px;
      border-radius: 12px !important;
      font-size: 0.95rem !important;
    }
    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
      gap: 1.5rem;
    }
    .portal-card {
      padding: 1.75rem;
      cursor: pointer;
      display: flex;
      flex-direction: column;
      height: 100%;
    }
    .card-icon {
      width: 52px;
      height: 52px;
      border-radius: 14px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      margin-bottom: 1.25rem;
    }
    .card-icon.indigo { background: linear-gradient(135deg, #6366f1, #4f46e5); }
    .card-icon.amber { background: linear-gradient(135deg, #f59e0b, #d97706); }
    .card-icon.teal { background: linear-gradient(135deg, #14b8a6, #0d9488); }
    .card-icon.purple { background: linear-gradient(135deg, #a855f7, #9333ea); }
    .card-icon mat-icon { font-size: 28px; width: 28px; height: 28px; }
    h3 {
      font-size: 1.25rem;
      font-weight: 700;
      margin: 0 0 0.5rem;
      color: #0f172a;
    }
    p {
      color: #64748b;
      font-size: 0.9rem;
      line-height: 1.5;
      margin: 0 0 1.5rem;
      flex-grow: 1;
    }
    .card-link {
      font-size: 0.875rem;
      font-weight: 700;
      color: #4f46e5;
    }
  `]
})
export class HomeComponent implements OnInit {
  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      if (this.authService.getUserRole() === 'ADMIN') {
        this.router.navigate(['/dashboard']);
      } else {
        this.router.navigate(['/attendance']);
      }
    }
  }
}