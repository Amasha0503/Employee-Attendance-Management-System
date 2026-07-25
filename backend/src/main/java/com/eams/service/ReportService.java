package com.eams.service;

import java.time.LocalDate;

public interface ReportService {
    byte[] generateDailyAttendanceCsv(LocalDate date);
    byte[] generateDailyAttendancePdf(LocalDate date);

    byte[] generateMonthlyAttendanceSummaryCsv(int year, int month);
    byte[] generateMonthlyAttendanceSummaryPdf(int year, int month);

    byte[] generateLeaveUtilizationCsv();
    byte[] generateLeaveUtilizationPdf();

    byte[] generateOvertimeReportCsv();
    byte[] generateOvertimeReportPdf();

    byte[] generateLateEarlyTrendCsv(LocalDate startDate, LocalDate endDate);
    byte[] generateLateEarlyTrendPdf(LocalDate startDate, LocalDate endDate);
}
