package com.eams.controller;

import com.eams.dto.*;
import com.eams.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    @GetMapping("/attendance-trend")
    public ResponseEntity<List<AttendanceTrendDto>> getAttendanceTrend(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getAttendanceTrend(startDate, endDate));
    }

    @GetMapping("/late-early-trend")
    public ResponseEntity<List<LateEarlyTrendDto>> getLateEarlyTrend(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getLateEarlyTrend(startDate, endDate));
    }

    @GetMapping("/leave-summary")
    public ResponseEntity<List<LeaveSummaryDto>> getLeaveSummary() {
        return ResponseEntity.ok(dashboardService.getLeaveSummary());
    }

    @GetMapping("/overtime-summary")
    public ResponseEntity<List<OvertimeSummaryDto>> getOvertimeSummary() {
        return ResponseEntity.ok(dashboardService.getOvertimeSummary());
    }
}
