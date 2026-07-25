import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSnackBarModule],
  template: `
    <div class="login-shell">
      <mat-card class="login-card">
        <h2>Employee Attendance System</h2>
        <p>Sign in to continue</p>
        <form [formGroup]="form" (ngSubmit)="submit()">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Username</mat-label>
            <input matInput formControlName="username" />
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Password</mat-label>
            <input matInput type="password" formControlName="password" />
          </mat-form-field>

          <button mat-raised-button color="primary" type="submit" class="full-width">Login</button>
        </form>
      </mat-card>
    </div>
  `,
  styles: [
    `.login-shell { min-height: 100vh; display: grid; place-items: center; background: #f5f7fb; }`,
    `.login-card { width: min(100%, 420px); padding: 1rem; }`,
    `.full-width { width: 100%; }`,
    `h2 { margin-bottom: 0.25rem; }`,
    `p { margin-bottom: 1rem; color: #68727d; }`
  ]
})
export class LoginComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }

    this.authService.login(this.form.value.username, this.form.value.password).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: () => {
        this.snackBar.open('Invalid username or password', 'Close', { duration: 3000 });
      }
    });
  }
}
