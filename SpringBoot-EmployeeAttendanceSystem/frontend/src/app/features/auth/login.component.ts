import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../core/auth.service';
import { NotificationService } from '../../shared/notification.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  template: `
    <div class="login-page">
      <!-- Decorative Background Blobs -->
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>

      <mat-card class="login-card">
        <div class="card-header-brand">
          <div class="brand-logo">
            <mat-icon>schedule</mat-icon>
          </div>
          <h1>Welcome Back</h1>
          <p class="subtitle">Employee Attendance Management System</p>
        </div>

        <form [formGroup]="form" (ngSubmit)="submit()" class="login-form">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Username</mat-label>
            <mat-icon matPrefix class="prefix-icon">person</mat-icon>
            <input matInput formControlName="username" placeholder="Enter your username" />
            <mat-error *ngIf="form.get('username')?.hasError('required')">Username is required</mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Password</mat-label>
            <mat-icon matPrefix class="prefix-icon">lock</mat-icon>
            <input matInput [type]="hidePassword ? 'password' : 'text'" formControlName="password" placeholder="Enter your password" />
            <button type="button" mat-icon-button matSuffix (click)="hidePassword = !hidePassword">
              <mat-icon>{{ hidePassword ? 'visibility_off' : 'visibility' }}</mat-icon>
            </button>
            <mat-error *ngIf="form.get('password')?.hasError('required')">Password is required</mat-error>
          </mat-form-field>

          <button mat-raised-button color="primary" type="submit" class="submit-btn" [disabled]="loading">
            <span *ngIf="!loading">Sign In to Account</span>
            <mat-spinner diameter="24" *ngIf="loading"></mat-spinner>
          </button>
        </form>

        <div class="demo-hints">
          <span class="hint-title">Quick Demo Credentials:</span>
          <div class="hint-chips">
            <span class="hint-chip" (click)="fillDemo('admin', 'admin123')">🔑 Admin: admin / admin123</span>
            <span class="hint-chip" (click)="fillDemo('john_doe', 'user123')">👤 Employee: john_doe / user123</span>
          </div>
        </div>
      </mat-card>
    </div>
  `,
  styles: [`
    .login-page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      background: radial-gradient(circle at 50% 10%, #1e1b4b 0%, #0f172a 100%);
      overflow: hidden;
      padding: 1.5rem;
    }
    .blob {
      position: absolute;
      border-radius: 50%;
      filter: blur(80px);
      opacity: 0.45;
    }
    .blob-1 {
      width: 400px;
      height: 400px;
      background: #4f46e5;
      top: -100px;
      left: -100px;
    }
    .blob-2 {
      width: 450px;
      height: 450px;
      background: #06b6d4;
      bottom: -150px;
      right: -100px;
    }
    .login-card {
      width: 100%;
      max-width: 440px;
      padding: 2.5rem 2rem;
      border-radius: 24px !important;
      background: rgba(255, 255, 255, 0.95) !important;
      backdrop-filter: blur(16px);
      box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35) !important;
      z-index: 10;
      border: 1px solid rgba(255, 255, 255, 0.3) !important;
    }
    .card-header-brand {
      text-align: center;
      margin-bottom: 2rem;
    }
    .brand-logo {
      width: 64px;
      height: 64px;
      margin: 0 auto 1.25rem;
      background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
      border-radius: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      box-shadow: 0 10px 25px rgba(79, 70, 229, 0.4);
    }
    .brand-logo mat-icon {
      font-size: 36px;
      width: 36px;
      height: 36px;
    }
    h1 {
      font-size: 1.75rem;
      font-weight: 800;
      color: #0f172a;
      margin: 0 0 0.25rem;
      letter-spacing: -0.02em;
    }
    .subtitle {
      color: #64748b;
      font-size: 0.9rem;
      margin: 0;
    }
    .login-form {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }
    .full-width {
      width: 100%;
    }
    .prefix-icon {
      color: #94a3b8;
      margin-right: 8px;
    }
    .submit-btn {
      height: 52px;
      font-size: 1rem !important;
      border-radius: 14px !important;
      margin-top: 1rem;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .demo-hints {
      margin-top: 2rem;
      padding-top: 1.5rem;
      border-top: 1px dashed #e2e8f0;
      text-align: center;
    }
    .hint-title {
      font-size: 0.75rem;
      font-weight: 700;
      color: #94a3b8;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      display: block;
      margin-bottom: 0.75rem;
    }
    .hint-chips {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }
    .hint-chip {
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      padding: 0.5rem 0.85rem;
      border-radius: 10px;
      font-size: 0.825rem;
      font-weight: 600;
      color: #334155;
      cursor: pointer;
      transition: all 0.2s ease;
    }
    .hint-chip:hover {
      background: #e0e7ff;
      border-color: #c7d2fe;
      color: #4338ca;
    }
  `]
})
export class LoginComponent {
  form: FormGroup;
  hidePassword = true;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  fillDemo(u: string, p: string): void {
    this.form.patchValue({ username: u, password: p });
  }

  submit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    this.authService.login(this.form.value.username, this.form.value.password).subscribe({
      next: () => {
        this.loading = false;
        this.notificationService.showSuccess('Welcome back! Login successful.');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.notificationService.showError('Invalid credentials. Please try again.');
      }
    });
  }
}
