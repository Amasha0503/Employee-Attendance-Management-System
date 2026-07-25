import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DashboardSummary {
  totalEmployees: number;
  presentToday: number;
  lateToday: number;
  absentToday: number;
  pendingLeaves: number;
  pendingOvertime: number;
}

export interface AttendanceTrend {
  date: string;
  presentCount: number;
  lateCount: number;
  absentCount: number;
}

export interface LateEarlyTrend {
  department: string;
  employeeName: string;
  lateCount: number;
  earlyDepartureCount: number;
}

export interface LeaveSummary {
  leaveTypeName: string;
  totalRequests: number;
  approvedRequests: number;
  pendingRequests: number;
  rejectedRequests: number;
  totalDaysTaken: number;
}

export interface OvertimeSummary {
  employeeId: number;
  employeeName: string;
  department: string;
  totalRequests: number;
  approvedHours: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = 'http://localhost:8080/api/v1/dashboard';

  constructor(private http: HttpClient) {}

  getSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.apiUrl}/summary`);
  }

  getAttendanceTrend(startDate?: string, endDate?: string): Observable<AttendanceTrend[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<AttendanceTrend[]>(`${this.apiUrl}/attendance-trend`, { params });
  }

  getLateEarlyTrend(startDate?: string, endDate?: string): Observable<LateEarlyTrend[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<LateEarlyTrend[]>(`${this.apiUrl}/late-early-trend`, { params });
  }

  getLeaveSummary(): Observable<LeaveSummary[]> {
    return this.http.get<LeaveSummary[]>(`${this.apiUrl}/leave-summary`);
  }

  getOvertimeSummary(): Observable<OvertimeSummary[]> {
    return this.http.get<OvertimeSummary[]>(`${this.apiUrl}/overtime-summary`);
  }
}
