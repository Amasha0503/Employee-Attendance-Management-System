package com.eams.service.impl;

import com.eams.dto.LateEarlyTrendDto;
import com.eams.dto.LeaveSummaryDto;
import com.eams.dto.OvertimeSummaryDto;
import com.eams.entity.Attendance;
import com.eams.repository.AttendanceRepository;
import com.eams.service.DashboardService;
import com.eams.service.ReportService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AttendanceRepository attendanceRepository;
    private final DashboardService dashboardService;

    // --- Daily Attendance ---
    @Override
    public byte[] generateDailyAttendanceCsv(LocalDate date) {
        if (date == null) date = LocalDate.now();
        List<Attendance> list = attendanceRepository.findByAttendanceDateBetween(date, date);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Attendance ID,Employee ID,Employee Name,Department,Date,Check In,Check Out,Status,Late,Early Departure");
            for (Attendance a : list) {
                String name = a.getEmployee() != null ? (a.getEmployee().getFirstName() + " " + (a.getEmployee().getLastName() != null ? a.getEmployee().getLastName() : "")) : "N/A";
                String dept = a.getEmployee() != null && a.getEmployee().getDepartment() != null ? a.getEmployee().getDepartment() : "N/A";
                writer.printf("%d,%d,\"%s\",\"%s\",%s,%s,%s,%s,%b,%b\n",
                        a.getAttendanceId(),
                        a.getEmployee() != null ? a.getEmployee().getEmployeeId() : 0,
                        name,
                        dept,
                        a.getAttendanceDate(),
                        a.getCheckIn() != null ? a.getCheckIn().toLocalTime() : "",
                        a.getCheckOut() != null ? a.getCheckOut().toLocalTime() : "",
                        a.getStatus(),
                        Boolean.TRUE.equals(a.getLateFlag()),
                        Boolean.TRUE.equals(a.getEarlyDeparture())
                );
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateDailyAttendancePdf(LocalDate date) {
        if (date == null) date = LocalDate.now();
        List<Attendance> list = attendanceRepository.findByAttendanceDateBetween(date, date);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Daily Attendance Sheet — " + date, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 3f, 2f, 2f, 2f, 2f});

            addTableHeader(table, new String[]{"Emp ID", "Name", "Check In", "Check Out", "Status", "Late/Early"});

            for (Attendance a : list) {
                String name = a.getEmployee() != null ? (a.getEmployee().getFirstName() + " " + (a.getEmployee().getLastName() != null ? a.getEmployee().getLastName() : "")) : "N/A";
                String checkIn = a.getCheckIn() != null ? a.getCheckIn().toLocalTime().toString() : "-";
                String checkOut = a.getCheckOut() != null ? a.getCheckOut().toLocalTime().toString() : "-";
                String flags = (Boolean.TRUE.equals(a.getLateFlag()) ? "Late " : "") + (Boolean.TRUE.equals(a.getEarlyDeparture()) ? "Early" : "");
                if (flags.isEmpty()) flags = "Normal";

                table.addCell(String.valueOf(a.getEmployee() != null ? a.getEmployee().getEmployeeId() : 0));
                table.addCell(name);
                table.addCell(checkIn);
                table.addCell(checkOut);
                table.addCell(a.getStatus() != null ? a.getStatus() : "-");
                table.addCell(flags);
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Daily Attendance PDF", e);
        }
        return out.toByteArray();
    }

    // --- Monthly Attendance Summary ---
    @Override
    public byte[] generateMonthlyAttendanceSummaryCsv(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Attendance> list = attendanceRepository.findByAttendanceDateBetween(start, end);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Year,Month,Total Records,Present Count,Late Count,Absent Count");
            long present = list.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
            long late = list.stream().filter(a -> "LATE".equalsIgnoreCase(a.getStatus()) || Boolean.TRUE.equals(a.getLateFlag())).count();
            long absent = list.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();

            writer.printf("%d,%d,%d,%d,%d,%d\n", year, month, list.size(), present, late, absent);
            writer.flush();
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateMonthlyAttendanceSummaryPdf(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Attendance> list = attendanceRepository.findByAttendanceDateBetween(start, end);
        long present = list.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        long late = list.stream().filter(a -> "LATE".equalsIgnoreCase(a.getStatus()) || Boolean.TRUE.equals(a.getLateFlag())).count();
        long absent = list.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Monthly Attendance Summary — " + ym.getMonth() + " " + year, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            addTableHeader(table, new String[]{"Metric", "Count"});

            table.addCell("Total Days Recorded");
            table.addCell(String.valueOf(list.size()));
            table.addCell("Present Days");
            table.addCell(String.valueOf(present));
            table.addCell("Late Arrivals");
            table.addCell(String.valueOf(late));
            table.addCell("Absent Days");
            table.addCell(String.valueOf(absent));

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Monthly Attendance PDF", e);
        }
        return out.toByteArray();
    }

    // --- Leave Utilization Report ---
    @Override
    public byte[] generateLeaveUtilizationCsv() {
        List<LeaveSummaryDto> list = dashboardService.getLeaveSummary();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Leave Type,Total Requests,Approved Requests,Pending Requests,Rejected Requests,Total Days Taken");
            for (LeaveSummaryDto dto : list) {
                writer.printf("\"%s\",%d,%d,%d,%d,%d\n",
                        dto.getLeaveTypeName(),
                        dto.getTotalRequests(),
                        dto.getApprovedRequests(),
                        dto.getPendingRequests(),
                        dto.getRejectedRequests(),
                        dto.getTotalDaysTaken()
                );
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateLeaveUtilizationPdf() {
        List<LeaveSummaryDto> list = dashboardService.getLeaveSummary();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Leave Utilization Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            addTableHeader(table, new String[]{"Leave Type", "Total", "Approved", "Pending", "Rejected", "Days Taken"});

            for (LeaveSummaryDto dto : list) {
                table.addCell(dto.getLeaveTypeName());
                table.addCell(String.valueOf(dto.getTotalRequests()));
                table.addCell(String.valueOf(dto.getApprovedRequests()));
                table.addCell(String.valueOf(dto.getPendingRequests()));
                table.addCell(String.valueOf(dto.getRejectedRequests()));
                table.addCell(String.valueOf(dto.getTotalDaysTaken()));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Leave Utilization PDF", e);
        }
        return out.toByteArray();
    }

    // --- Overtime Report ---
    @Override
    public byte[] generateOvertimeReportCsv() {
        List<OvertimeSummaryDto> list = dashboardService.getOvertimeSummary();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Employee ID,Employee Name,Department,Total Requests,Approved Hours");
            for (OvertimeSummaryDto dto : list) {
                writer.printf("%d,\"%s\",\"%s\",%d,%s\n",
                        dto.getEmployeeId(),
                        dto.getEmployeeName(),
                        dto.getDepartment(),
                        dto.getTotalRequests(),
                        dto.getApprovedHours() != null ? dto.getApprovedHours().toString() : "0.00"
                );
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateOvertimeReportPdf() {
        List<OvertimeSummaryDto> list = dashboardService.getOvertimeSummary();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Overtime Summary Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            addTableHeader(table, new String[]{"Emp ID", "Employee Name", "Department", "Requests", "Approved Hours"});

            for (OvertimeSummaryDto dto : list) {
                table.addCell(String.valueOf(dto.getEmployeeId()));
                table.addCell(dto.getEmployeeName());
                table.addCell(dto.getDepartment());
                table.addCell(String.valueOf(dto.getTotalRequests()));
                table.addCell(dto.getApprovedHours() != null ? dto.getApprovedHours().toString() : "0.00");
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Overtime PDF", e);
        }
        return out.toByteArray();
    }

    // --- Late / Early Trend ---
    @Override
    public byte[] generateLateEarlyTrendCsv(LocalDate startDate, LocalDate endDate) {
        List<LateEarlyTrendDto> list = dashboardService.getLateEarlyTrend(startDate, endDate);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out)) {
            writer.println("Department,Employee Name,Late Count,Early Departure Count");
            for (LateEarlyTrendDto dto : list) {
                writer.printf("\"%s\",\"%s\",%d,%d\n",
                        dto.getDepartment(),
                        dto.getEmployeeName(),
                        dto.getLateCount(),
                        dto.getEarlyDepartureCount()
                );
            }
            writer.flush();
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateLateEarlyTrendPdf(LocalDate startDate, LocalDate endDate) {
        List<LateEarlyTrendDto> list = dashboardService.getLateEarlyTrend(startDate, endDate);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Late Arrival & Early Departure Trend Analysis", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            addTableHeader(table, new String[]{"Department", "Employee Name", "Late Arrivals", "Early Departures"});

            for (LateEarlyTrendDto dto : list) {
                table.addCell(dto.getDepartment());
                table.addCell(dto.getEmployeeName());
                table.addCell(String.valueOf(dto.getLateCount()));
                table.addCell(String.valueOf(dto.getEarlyDepartureCount()));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Late/Early Trend PDF", e);
        }
        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
            cell.setBackgroundColor(new Color(63, 81, 181));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            table.addCell(cell);
        }
    }
}
