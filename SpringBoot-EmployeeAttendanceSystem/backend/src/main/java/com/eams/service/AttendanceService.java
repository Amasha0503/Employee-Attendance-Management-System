package com.eams.service;

import com.eams.dto.AttendanceRequest;
import com.eams.dto.AttendanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn(AttendanceRequest request);
    AttendanceResponse checkOut(AttendanceRequest request);
    List<AttendanceResponse> getEmployeeHistory(Integer employeeId);
    List<AttendanceResponse> getAllAttendance(LocalDate startDate, LocalDate endDate);
}
