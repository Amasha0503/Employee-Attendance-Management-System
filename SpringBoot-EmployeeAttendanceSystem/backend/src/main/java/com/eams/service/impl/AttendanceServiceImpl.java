package com.eams.service.impl;

import com.eams.dto.AttendanceRequest;
import com.eams.dto.AttendanceResponse;
import com.eams.entity.Attendance;
import com.eams.entity.Employee;
import com.eams.entity.Shift;
import com.eams.repository.AttendanceRepository;
import com.eams.repository.EmployeeRepository;
import com.eams.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public AttendanceResponse checkIn(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeEmployeeIdAndAttendanceDate(employee.getEmployeeId(), today)
                .orElseGet(() -> createAttendance(employee, today));

        attendance.setCheckIn(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now());
        attendance.setStatus(determineStatus(attendance.getCheckIn(), employee.getShift()));
        attendance.setLateFlag(attendance.getStatus().equals("LATE"));
        return mapToResponse(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeEmployeeIdAndAttendanceDate(employee.getEmployeeId(), today)
                .orElseGet(() -> createAttendance(employee, today));

        attendance.setCheckOut(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now());
        attendance.setEarlyDeparture(attendance.getCheckOut().toLocalTime().isBefore(getShiftEnd(employee.getShift())));
        if (attendance.getStatus() == null || attendance.getStatus().equals("PRESENT")) {
            attendance.setStatus(attendance.getEarlyDeparture() ? "EARLY_DEPARTURE" : "PRESENT");
        }
        return mapToResponse(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getEmployeeHistory(Integer employeeId) {
        return attendanceRepository.findByEmployeeEmployeeIdOrderByAttendanceDateDesc(employeeId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendance(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByAttendanceDateBetween(startDate, endDate).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private Attendance createAttendance(Employee employee, LocalDate date) {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setAttendanceDate(date);
        attendance.setStatus("PENDING");
        return attendance;
    }

    private String determineStatus(LocalDateTime checkIn, Shift shift) {
        if (shift == null) {
            return "PRESENT";
        }
        LocalTime start = shift.getStartTime();
        LocalTime grace = shift.getGraceMinutes() != null ? shift.getGraceMinutes().longValue() : 10L;
        LocalTime allowed = start.plusMinutes(grace);
        return checkIn.toLocalTime().isAfter(allowed) ? "LATE" : "PRESENT";
    }

    private LocalTime getShiftEnd(Shift shift) {
        return shift != null ? shift.getEndTime() : LocalTime.of(17, 0);
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getAttendanceId(),
                attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeId() : null,
                attendance.getAttendanceDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut(),
                attendance.getStatus(),
                attendance.getLateFlag(),
                attendance.getEarlyDeparture()
        );
    }
}
