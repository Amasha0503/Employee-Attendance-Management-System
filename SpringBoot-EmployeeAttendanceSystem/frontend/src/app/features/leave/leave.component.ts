import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-leave',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatTableModule, MatButtonModule, MatInputModule, MatSnackBarModule],
  templateUrl: './leave.component.html'
})
export class LeaveComponent implements OnInit {
  form!: FormGroup;
  leaves: any[] = [];
  displayedColumns = ['startDate', 'endDate', 'status'];

  constructor(private http: HttpClient, private fb: FormBuilder, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      employeeId: [1, Validators.required],
      leaveTypeId: [1, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      reason: ['']
    });
    this.loadLeaves();
  }

  submit(): void {
    this.http.post('/api/v1/leave', this.form.value).subscribe({
      next: () => {
        this.snackBar.open('Leave request submitted', 'Close', { duration: 3000 });
        this.loadLeaves();
      },
      error: () => this.snackBar.open('Failed to submit leave', 'Close', { duration: 3000 })
    });
  }

  private loadLeaves(): void {
    this.http.get<any[]>('/api/v1/leave/employee/1').subscribe((data) => this.leaves = data);
  }
}
