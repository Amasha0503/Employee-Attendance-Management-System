import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from '../../shared/notification.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTabsModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  employees: any[] = [];
  shifts: any[] = [];
  holidays: any[] = [];

  employeeForm!: FormGroup;
  shiftForm!: FormGroup;
  holidayForm!: FormGroup;

  displayedColumns = ['employeeId', 'name', 'email', 'department', 'status', 'actions'];

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.employeeForm = this.fb.group({
      employeeId: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      department: ['', Validators.required],
      shiftId: [''],
      status: ['ACTIVE']
    });

    this.shiftForm = this.fb.group({
      shiftName: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      graceMinutes: [10, Validators.required]
    });

    this.holidayForm = this.fb.group({
      holidayName: ['', Validators.required],
      holidayDate: ['', Validators.required]
    });

    this.loadData();
  }

  loadData(): void {
    this.http.get<any[]>('http://localhost:8080/api/v1/admin/employees').subscribe({
      next: (data) => this.employees = data,
      error: (err) => console.error(err)
    });
    this.http.get<any[]>('http://localhost:8080/api/v1/admin/shifts').subscribe({
      next: (data) => this.shifts = data,
      error: (err) => console.error(err)
    });
    this.http.get<any[]>('http://localhost:8080/api/v1/admin/holidays').subscribe({
      next: (data) => this.holidays = data,
      error: (err) => console.error(err)
    });
  }

  createEmployee(): void {
    if (this.employeeForm.invalid) {
      this.notificationService.showError('Please complete all required employee fields.');
      return;
    }

    this.http.post('http://localhost:8080/api/v1/admin/employees', this.employeeForm.value).subscribe({
      next: () => {
        this.notificationService.showSuccess('Employee profile saved successfully!');
        this.employeeForm.reset({ status: 'ACTIVE' });
        this.loadData();
      },
      error: () => this.notificationService.showError('Failed to save employee profile.')
    });
  }

  deactivateEmployee(id: number): void {
    this.http.delete(`http://localhost:8080/api/v1/admin/employees/${id}`).subscribe({
      next: () => {
        this.notificationService.showSuccess('Employee status set to INACTIVE');
        this.loadData();
      },
      error: () => this.notificationService.showError('Deactivation failed.')
    });
  }

  createShift(): void {
    if (this.shiftForm.invalid) {
      this.notificationService.showError('Please complete all required shift fields.');
      return;
    }

    this.http.post('http://localhost:8080/api/v1/admin/shifts', this.shiftForm.value).subscribe({
      next: () => {
        this.notificationService.showSuccess('Work shift configured successfully!');
        this.shiftForm.reset({ graceMinutes: 10 });
        this.loadData();
      },
      error: () => this.notificationService.showError('Failed to configure shift.')
    });
  }

  deleteShift(id: number): void {
    this.http.delete(`http://localhost:8080/api/v1/admin/shifts/${id}`).subscribe({
      next: () => {
        this.notificationService.showSuccess('Shift configuration deleted.');
        this.loadData();
      },
      error: () => this.notificationService.showError('Failed to delete shift.')
    });
  }

  createHoliday(): void {
    if (this.holidayForm.invalid) {
      this.notificationService.showError('Please complete holiday details.');
      return;
    }

    this.http.post('http://localhost:8080/api/v1/admin/holidays', this.holidayForm.value).subscribe({
      next: () => {
        this.notificationService.showSuccess('Holiday added to company calendar!');
        this.holidayForm.reset();
        this.loadData();
      },
      error: () => this.notificationService.showError('Failed to add holiday.')
    });
  }

  deleteHoliday(id: number): void {
    this.http.delete(`http://localhost:8080/api/v1/admin/holidays/${id}`).subscribe({
      next: () => {
        this.notificationService.showSuccess('Holiday removed from calendar.');
        this.loadData();
      },
      error: () => this.notificationService.showError('Failed to remove holiday.')
    });
  }
}
