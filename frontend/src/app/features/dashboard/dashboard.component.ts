import { Component, ElementRef, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DashboardService, DashboardSummary, AttendanceTrend, LateEarlyTrend, LeaveSummary, OvertimeSummary } from '../../core/dashboard.service';
import { ReportService } from '../../core/report.service';
import { AuthService } from '../../core/auth.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTabsModule,
    MatTableModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {
  summary?: DashboardSummary;
  attendanceTrend: AttendanceTrend[] = [];
  lateEarlyTrend: LateEarlyTrend[] = [];
  leaveSummary: LeaveSummary[] = [];
  overtimeSummary: OvertimeSummary[] = [];
  
  loading = true;
  isAdmin = false;

  @ViewChild('attendanceChartCanvas') attendanceChartCanvas!: ElementRef<HTMLCanvasElement>;
  attendanceChart?: Chart;

  constructor(
    private dashboardService: DashboardService,
    private reportService: ReportService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getUserRole() === 'ADMIN';
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    // Canvas initialized after view loads
  }

  loadDashboardData(): void {
    this.loading = true;
    this.dashboardService.getSummary().subscribe({
      next: (res) => {
        this.summary = res;
      },
      error: (err) => console.error(err)
    });

    this.dashboardService.getAttendanceTrend().subscribe({
      next: (res) => {
        this.attendanceTrend = res;
        this.renderAttendanceChart();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });

    if (this.isAdmin) {
      this.dashboardService.getLateEarlyTrend().subscribe(res => this.lateEarlyTrend = res);
      this.dashboardService.getLeaveSummary().subscribe(res => this.leaveSummary = res);
      this.dashboardService.getOvertimeSummary().subscribe(res => this.overtimeSummary = res);
    }
  }

  renderAttendanceChart(): void {
    if (!this.attendanceChartCanvas) return;

    const labels = this.attendanceTrend.map(t => t.date);
    const presentData = this.attendanceTrend.map(t => t.presentCount);
    const lateData = this.attendanceTrend.map(t => t.lateCount);
    const absentData = this.attendanceTrend.map(t => t.absentCount);

    if (this.attendanceChart) {
      this.attendanceChart.destroy();
    }

    this.attendanceChart = new Chart(this.attendanceChartCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          { label: 'Present', data: presentData, backgroundColor: '#4caf50' },
          { label: 'Late', data: lateData, backgroundColor: '#ff9800' },
          { label: 'Absent', data: absentData, backgroundColor: '#f44336' }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'top' },
          title: { display: true, text: '30-Day Attendance Trend' }
        },
        scales: {
          x: { stacked: true },
          y: { stacked: true, beginAtZero: true }
        }
      }
    });
  }

  downloadReport(type: any, format: 'csv' | 'pdf'): void {
    this.reportService.exportReport(type, format);
  }
}
