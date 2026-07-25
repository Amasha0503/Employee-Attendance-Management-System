import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatTableModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatSnackBarModule],
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {
  employees: any[] = [];
  shifts: any[] = [];
  holidays: any[] = [];

  employeeForm!: FormGroup;
  shiftForm!: FormGroup;
  holidayForm!: FormGroup;

  displayedColumns = ['employeeId', 'name', 'department', 'status', 'actions'];

  constructor(private http: HttpClient, private fb: FormBuilder, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.employeeForm = this.fb.group({
      employeeId: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      department: ['', Validators.required],
      designation: ['', Validators.required],
      shiftId: [''],
      status: ['ACTIVE']
    });

    this.shiftForm = this.fb.group({
      shiftName: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      graceMinutes: [10]
    });

    this.holidayForm = this.fb.group({
      holidayName: ['', Validators.required],
      holidayDate: ['', Validators.required]
    });

    this.loadData();
  }

  loadData(): void {
    this.http.get<any[]>('/api/v1/admin/employees').subscribe((data) => this.employees = data);
    this.http.get<any[]>('/api/v1/admin/shifts').subscribe((data) => this.shifts = data);
    this.http.get<any[]>('/api/v1/admin/holidays').subscribe((data) => this.holidays = data);
  }

  createEmployee(): void {
    if (this.employeeForm.invalid) {
      this.snackBar.open('Please complete all required fields', 'Close', { duration: 3000 });
      return;
    }

    this.http.post('/api/v1/admin/employees', this.employeeForm.value).subscribe({
      next: () => {
        this.snackBar.open('Employee saved', 'Close', { duration: 3000 });
        this.employeeForm.reset({ status: 'ACTIVE' });
        this.loadData();
      },
      error: () => this.snackBar.open('Failed to save employee', 'Close', { duration: 3000 })
    });
  }

  deactivateEmployee(id: number): void {
    this.http.delete(`/api/v1/admin/employees/${id}`).subscribe({
      next: () => {
        this.snackBar.open('Employee deactivated', 'Close', { duration: 3000 });
        this.loadData();
      },
      error: () => this.snackBar.open('Failed to deactivate employee', 'Close', { duration: 3000 })
    });
  }

  createShift(): void {
    if (this.shiftForm.invalid) {
      this.snackBar.open('Please complete all required fields', 'Close', { duration: 3000 });
      return;
    }

    this.http.post('/api/v1/admin/shifts', this.shiftForm.value).subscribe({
      next: () => {
        this.snackBar.open('Shift saved', 'Close', { duration: 3000 });
        this.shiftForm.reset();
        this.loadData();
      },
      error: () => this.snackBar.open('Failed to save shift', 'Close', { duration: 3000 })
    });
  }

  deleteShift(id: number): void {
    this.http.delete(`/api/v1/admin/shifts/${id}`).subscribe({
      next: () => {
        this.snackBar.open('Shift deleted', 'Close', { duration: 3000 });
        this.loadData();
      },
      error: () => this.snackBar.open('Failed to delete shift', 'Close', { duration: 3000 })
    });
  }

  createHoliday(): void {
    if (this.holidayForm.invalid) {
      this.snackBar.open('Please complete all required fields', 'Close', { duration: 3000 });
      return;
    }

    this.http.post('/api/v1/admin/holidays', this.holidayForm.value).subscribe({
      next: () => {
        this.snackBar.open('Holiday saved', 'Close', { duration: 3000 });
        this.holidayForm.reset();
        this.loadData();
      },
      error: () => this.snackBar.open('Failed to save holiday', 'Close', { duration: 3000 })
    });
  }

  deleteHoliday(id: number): void {
    this.http.delete(`/api/v1/admin/holidays/${id}`).subscribe({
      next: () => {
        this.snackBar.open('Holiday deleted', 'Close', { duration: 3000 });
        this.loadData();
      },
      error: () => this.snackBar.open('Failed to delete holiday', 'Close', { duration: 3000 })
    });
  }
}
