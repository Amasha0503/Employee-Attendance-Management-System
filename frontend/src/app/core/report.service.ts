import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = 'http://localhost:8080/api/v1/reports';

  constructor(private http: HttpClient) {}

  exportReport(type: 'daily-attendance' | 'monthly-summary' | 'leave-utilization' | 'overtime-report' | 'late-early-trend', format: 'csv' | 'pdf', dateParams?: { date?: string; year?: number; month?: number; startDate?: string; endDate?: string }): void {
    let params = new HttpParams();
    if (dateParams) {
      if (dateParams.date) params = params.set('date', dateParams.date);
      if (dateParams.year) params = params.set('year', dateParams.year.toString());
      if (dateParams.month) params = params.set('month', dateParams.month.toString());
      if (dateParams.startDate) params = params.set('startDate', dateParams.startDate);
      if (dateParams.endDate) params = params.set('endDate', dateParams.endDate);
    }

    const url = `${this.apiUrl}/export/${format}/${type}`;
    this.http.get(url, { params, responseType: 'blob' }).subscribe({
      next: (blob: Blob) => {
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        a.href = objectUrl;
        a.download = `${type}_report.${format}`;
        a.click();
        URL.revokeObjectURL(objectUrl);
      },
      error: (err) => {
        console.error('Report download failed', err);
      }
    });
  }
}
