import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-overtime',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatTableModule, MatButtonModule, MatInputModule, MatSnackBarModule],
  templateUrl: './overtime.component.html'
})
export class OvertimeComponent implements OnInit {
  form!: FormGroup;
  overtimeList: any[] = [];
  displayedColumns = ['otDate', 'hours', 'status'];

  constructor(private http: HttpClient, private fb: FormBuilder, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      employeeId: [1, Validators.required],
      otDate: ['', Validators.required],
      hours: ['', Validators.required],
      reason: ['']
    });
    this.loadOvertime();
  }

  submit(): void {
    this.http.post('/api/v1/overtime', this.form.value).subscribe({
      next: () => {
        this.snackBar.open('Overtime request submitted', 'Close', { duration: 3000 });
        this.loadOvertime();
      },
      error: () => this.snackBar.open('Failed to submit overtime', 'Close', { duration: 3000 })
    });
  }

  private loadOvertime(): void {
    this.http.get<any[]>('/api/v1/overtime/employee/1').subscribe((data) => this.overtimeList = data);
  }
}
