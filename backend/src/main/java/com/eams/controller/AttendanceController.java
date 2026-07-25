package com.eams.controller;

import com.eams.dto.AttendanceRequest;
import com.eams.dto.AttendanceResponse;
import com.eams.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.checkIn(request));
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.checkOut(request));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceResponse>> getEmployeeHistory(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(attendanceService.getEmployeeHistory(employeeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAllAttendance(startDate, endDate));
    }
}
