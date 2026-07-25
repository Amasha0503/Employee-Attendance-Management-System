import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../core/auth.service';
import { NotificationService } from '../../shared/notification.service';

@Component({
  selector: 'app-leave',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './leave.component.html',
  styleUrls: ['./leave.component.css']
})
export class LeaveComponent implements OnInit {
  form!: FormGroup;
  leaves: any[] = [];
  allLeaves: any[] = [];
  displayedColumns = ['leaveTypeName', 'startDate', 'endDate', 'reason', 'status', 'actions'];
  
  isAdmin = false;
  loading = false;

  leaveTypes = [
    { id: 1, name: 'Annual Leave' },
    { id: 2, name: 'Sick Leave' },
    { id: 3, name: 'Casual Leave' }
  ];

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
      leaveTypeId: [1, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      reason: ['', Validators.required]
    });

    this.loadLeaves();
  }

  submit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    this.http.post<any>('http://localhost:8080/api/v1/leave/apply', this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.notificationService.showSuccess('Leave request submitted successfully!');
        this.form.patchValue({ startDate: '', endDate: '', reason: '' });
        this.loadLeaves();
      },
      error: () => {
        this.loading = false;
        this.notificationService.showError('Failed to submit leave request.');
      }
    });
  }

  approveOrReject(requestId: number, status: string): void {
    this.http.put<any>(`http://localhost:8080/api/v1/leave/approve/${requestId}?status=${status}`, {}).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Leave request marked as ${status}`);
        this.loadLeaves();
      },
      error: () => this.notificationService.showError('Action failed.')
    });
  }

  private loadLeaves(): void {
    const url = this.isAdmin 
      ? 'http://localhost:8080/api/v1/leave/all' 
      : 'http://localhost:8080/api/v1/leave/employee/1';

    this.http.get<any[]>(url).subscribe({
      next: (data) => this.leaves = data,
      error: (err) => console.error(err)
    });
  }
}
