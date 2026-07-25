import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NotificationService } from '../../shared/notification.service';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit, OnDestroy {
  history: any[] = [];
  displayedColumns = ['attendanceDate', 'checkIn', 'checkOut', 'status', 'flags'];
  
  currentTime: string = '';
  currentDate: string = '';
  private timer: any;
  loading = false;

  constructor(
    private http: HttpClient,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.updateClock();
    this.timer = setInterval(() => this.updateClock(), 1000);
    this.loadHistory();
  }

  ngOnDestroy(): void {
    if (this.timer) clearInterval(this.timer);
  }

  updateClock(): void {
    const now = new Date();
    this.currentTime = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    this.currentDate = now.toLocaleDateString([], { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
  }

  checkIn(): void {
    this.loading = true;
    this.http.post<any>('http://localhost:8080/api/v1/attendance/check-in', { employeeId: 1 }).subscribe({
      next: (res) => {
        this.loading = false;
        this.notificationService.showSuccess(`Checked in successfully at ${new Date().toLocaleTimeString()}`);
        this.loadHistory();
      },
      error: (err) => {
        this.loading = false;
        this.notificationService.showError('Check-in failed. Please try again.');
      }
    });
  }

  checkOut(): void {
    this.loading = true;
    this.http.post<any>('http://localhost:8080/api/v1/attendance/check-out', { employeeId: 1 }).subscribe({
      next: (res) => {
        this.loading = false;
        this.notificationService.showSuccess(`Checked out successfully at ${new Date().toLocaleTimeString()}`);
        this.loadHistory();
      },
      error: (err) => {
        this.loading = false;
        this.notificationService.showError('Check-out failed. Please try again.');
      }
    });
  }

  private loadHistory(): void {
    this.http.get<any[]>('http://localhost:8080/api/v1/attendance/employee/1').subscribe({
      next: (data) => this.history = data,
      error: (err) => console.error(err)
    });
  }
}
