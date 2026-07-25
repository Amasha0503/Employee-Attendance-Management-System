package com.eams.controller;

import com.eams.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // --- Daily Attendance Sheet ---
    @GetMapping("/export/csv/daily-attendance")
    public ResponseEntity<byte[]> exportDailyAttendanceCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        byte[] data = reportService.generateDailyAttendanceCsv(date);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily_attendance.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/export/pdf/daily-attendance")
    public ResponseEntity<byte[]> exportDailyAttendancePdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        byte[] data = reportService.generateDailyAttendancePdf(date);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily_attendance.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    // --- Monthly Attendance Summary ---
    @GetMapping("/export/csv/monthly-summary")
    public ResponseEntity<byte[]> exportMonthlySummaryCsv(
            @RequestParam(defaultValue = "2026") int year,
            @RequestParam(defaultValue = "7") int month) {
        byte[] data = reportService.generateMonthlyAttendanceSummaryCsv(year, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_attendance_summary.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/export/pdf/monthly-summary")
    public ResponseEntity<byte[]> exportMonthlySummaryPdf(
            @RequestParam(defaultValue = "2026") int year,
            @RequestParam(defaultValue = "7") int month) {
        byte[] data = reportService.generateMonthlyAttendanceSummaryPdf(year, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_attendance_summary.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    // --- Leave Utilization Report ---
    @GetMapping("/export/csv/leave-utilization")
    public ResponseEntity<byte[]> exportLeaveUtilizationCsv() {
        byte[] data = reportService.generateLeaveUtilizationCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leave_utilization.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/export/pdf/leave-utilization")
    public ResponseEntity<byte[]> exportLeaveUtilizationPdf() {
        byte[] data = reportService.generateLeaveUtilizationPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leave_utilization.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    // --- Overtime Report ---
    @GetMapping("/export/csv/overtime-report")
    public ResponseEntity<byte[]> exportOvertimeReportCsv() {
        byte[] data = reportService.generateOvertimeReportCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=overtime_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/export/pdf/overtime-report")
    public ResponseEntity<byte[]> exportOvertimeReportPdf() {
        byte[] data = reportService.generateOvertimeReportPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=overtime_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    // --- Late / Early Trend Analysis ---
    @GetMapping("/export/csv/late-early-trend")
    public ResponseEntity<byte[]> exportLateEarlyTrendCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] data = reportService.generateLateEarlyTrendCsv(startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=late_early_trend.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/export/pdf/late-early-trend")
    public ResponseEntity<byte[]> exportLateEarlyTrendPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] data = reportService.generateLateEarlyTrendPdf(startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=late_early_trend.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}
