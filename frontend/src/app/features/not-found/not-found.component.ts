import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [CommonModule, RouterLink, MatButtonModule, MatCardModule],
  template: `
    <div class="not-found-container">
      <mat-card class="not-found-card">
        <mat-card-header>
          <mat-card-title>404 — Page Not Found</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <p>The page you are looking for does not exist or has been moved.</p>
        </mat-card-content>
        <mat-card-actions>
          <button mat-raised-button color="primary" routerLink="/">Return Home</button>
        </mat-card-actions>
      </mat-card>
    </div>
  `,
  styles: [`
    .not-found-container {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 80vh;
    }
    .not-found-card {
      max-width: 400px;
      padding: 2rem;
      text-align: center;
    }
  `]
})
export class NotFoundComponent { }
