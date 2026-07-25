import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../core/auth.service';
import { NotificationService } from '../../shared/notification.service';

@Component({
  selector: 'app-overtime',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './overtime.component.html',
  styleUrls: ['./overtime.component.css']
})
export class OvertimeComponent implements OnInit {
  form!: FormGroup;
  overtimeList: any[] = [];
  displayedColumns = ['otDate', 'hours', 'reason', 'status', 'actions'];
  
  isAdmin = false;
  loading = false;

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    public authService: AuthService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getUserRole() === 'ADMIN';

    this.form = this.fb.group({
      employeeId: [1, Validators.required],
      otDate: ['', Validators.required],
      hours: ['', [Validators.required, Validators.min(0.5), Validators.max(12)]],
      reason: ['', Validators.required]
    });

    this.loadOvertime();
  }

  submit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    this.http.post<any>('http://localhost:8080/api/v1/overtime/apply', this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.notificationService.showSuccess('Overtime request submitted!');
        this.form.patchValue({ otDate: '', hours: '', reason: '' });
        this.loadOvertime();
      },
      error: () => {
        this.loading = false;
        this.notificationService.showError('Failed to submit overtime request.');
      }
    });
  }

  approveOrReject(overtimeId: number, status: string): void {
    this.http.put<any>(`http://localhost:8080/api/v1/overtime/approve/${overtimeId}?status=${status}`, {}).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Overtime request marked as ${status}`);
        this.loadOvertime();
      },
      error: () => this.notificationService.showError('Action failed.')
    });
  }

  private loadOvertime(): void {
    const url = this.isAdmin 
      ? 'http://localhost:8080/api/v1/overtime/all' 
      : 'http://localhost:8080/api/v1/overtime/employee/1';

    this.http.get<any[]>(url).subscribe({
      next: (data) => this.overtimeList = data,
      error: (err) => console.error(err)
    });
  }
}
