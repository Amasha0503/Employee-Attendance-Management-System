import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatSnackBarModule],
  templateUrl: './attendance.component.html'
})
export class AttendanceComponent implements OnInit {
  history: any[] = [];
  displayedColumns = ['attendanceDate', 'status', 'lateFlag', 'earlyDeparture'];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadHistory();
  }

  checkIn(): void {
    this.http.post('/api/v1/attendance/check-in', { employeeId: 1 }).subscribe({
      next: () => {
        this.snackBar.open('Checked in', 'Close', { duration: 3000 });
        this.loadHistory();
      },
      error: () => this.snackBar.open('Check-in failed', 'Close', { duration: 3000 })
    });
  }

  checkOut(): void {
    this.http.post('/api/v1/attendance/check-out', { employeeId: 1 }).subscribe({
      next: () => {
        this.snackBar.open('Checked out', 'Close', { duration: 3000 });
        this.loadHistory();
      },
      error: () => this.snackBar.open('Check-out failed', 'Close', { duration: 3000 })
    });
  }

  private loadHistory(): void {
    this.http.get<any[]>('/api/v1/attendance/employee/1').subscribe((data) => this.history = data);
  }
}
