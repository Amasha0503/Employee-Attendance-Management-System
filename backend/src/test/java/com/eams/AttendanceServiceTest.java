package com.eams;

import com.eams.dto.AttendanceRequest;
import com.eams.dto.AttendanceResponse;
import com.eams.entity.Attendance;
import com.eams.entity.Employee;
import com.eams.entity.Shift;
import com.eams.repository.AttendanceRepository;
import com.eams.repository.EmployeeRepository;
import com.eams.service.impl.AttendanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Employee employee;
    private Shift shift;

    @BeforeEach
    public void setUp() {
        shift = new Shift();
        shift.setShiftId(1);
        shift.setShiftName("Morning Shift");
        shift.setStartTime(LocalTime.of(9, 0));
        shift.setEndTime(LocalTime.of(17, 0));
        shift.setGraceMinutes(10);

        employee = new Employee();
        employee.setEmployeeId(101);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setShift(shift);
    }

    @Test
    public void testCheckIn_OnTime() {
        AttendanceRequest request = new AttendanceRequest();
        request.setEmployeeId(101);
        request.setTimestamp(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 5))); // Within 10 min grace

        when(employeeRepository.findById(101)).thenReturn(Optional.of(employee));
        when(attendanceRepository.findByEmployeeEmployeeIdAndAttendanceDate(101, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceResponse response = attendanceService.checkIn(request);

        assertNotNull(response);
        assertEquals("PRESENT", response.getStatus());
        assertFalse(response.getLateFlag());
    }

    @Test
    public void testCheckIn_LateArrival() {
        AttendanceRequest request = new AttendanceRequest();
        request.setEmployeeId(101);
        request.setTimestamp(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 15))); // After 9:10 grace

        when(employeeRepository.findById(101)).thenReturn(Optional.of(employee));
        when(attendanceRepository.findByEmployeeEmployeeIdAndAttendanceDate(101, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceResponse response = attendanceService.checkIn(request);

        assertNotNull(response);
        assertEquals("LATE", response.getStatus());
        assertTrue(response.getLateFlag());
    }

    @Test
    public void testCheckOut_EarlyDeparture() {
        AttendanceRequest request = new AttendanceRequest();
        request.setEmployeeId(101);
        request.setTimestamp(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 30))); // Before 17:00

        Attendance attendance = new Attendance();
        attendance.setAttendanceId(1);
        attendance.setEmployee(employee);
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setStatus("PRESENT");

        when(employeeRepository.findById(101)).thenReturn(Optional.of(employee));
        when(attendanceRepository.findByEmployeeEmployeeIdAndAttendanceDate(101, LocalDate.now()))
                .thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceResponse response = attendanceService.checkOut(request);

        assertNotNull(response);
        assertTrue(response.getEarlyDeparture());
        assertEquals("EARLY_DEPARTURE", response.getStatus());
    }
}
