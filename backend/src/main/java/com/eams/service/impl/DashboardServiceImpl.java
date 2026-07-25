package com.eams.service.impl;

import com.eams.dto.*;
import com.eams.entity.Attendance;
import com.eams.entity.Employee;
import com.eams.entity.LeaveRequest;
import com.eams.entity.Overtime;
import com.eams.repository.*;
import com.eams.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final OvertimeRepository overtimeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public DashboardSummaryResponse getDashboardSummary() {
        LocalDate today = LocalDate.now();
        long totalEmployees = employeeRepository.countByStatus("ACTIVE");
        
        List<Attendance> todayAttendance = attendanceRepository.findByAttendanceDateBetween(today, today);
        long presentToday = todayAttendance.stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()) || "LATE".equalsIgnoreCase(a.getStatus()))
                .count();
        long lateToday = todayAttendance.stream()
                .filter(a -> Boolean.TRUE.equals(a.getLateFlag()))
                .count();
        long absentToday = todayAttendance.stream()
                .filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus()))
                .count();

        long pendingLeaves = leaveRequestRepository.findByStatus("PENDING").size();
        long pendingOvertime = overtimeRepository.findByStatus("PENDING").size();

        return DashboardSummaryResponse.builder()
                .totalEmployees(totalEmployees)
                .presentToday(presentToday)
                .lateToday(lateToday)
                .absentToday(absentToday)
                .pendingLeaves(pendingLeaves)
                .pendingOvertime(pendingOvertime)
                .build();
    }

    @Override
    public List<AttendanceTrendDto> getAttendanceTrend(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        List<Attendance> records = attendanceRepository.findByAttendanceDateBetween(startDate, endDate);
        Map<LocalDate, List<Attendance>> groupedByDate = records.stream()
                .collect(Collectors.groupingBy(Attendance::getAttendanceDate));

        List<AttendanceTrendDto> result = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            List<Attendance> list = groupedByDate.getOrDefault(current, Collections.emptyList());
            long present = list.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
            long late = list.stream().filter(a -> "LATE".equalsIgnoreCase(a.getStatus()) || Boolean.TRUE.equals(a.getLateFlag())).count();
            long absent = list.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();

            result.add(AttendanceTrendDto.builder()
                    .date(current)
                    .presentCount(present)
                    .lateCount(late)
                    .absentCount(absent)
                    .build());

            current = current.plusDays(1);
        }
        return result;
    }

    @Override
    public List<LateEarlyTrendDto> getLateEarlyTrend(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        List<Attendance> records = attendanceRepository.findByAttendanceDateBetween(startDate, endDate);
        Map<Employee, List<Attendance>> groupedByEmp = records.stream()
                .collect(Collectors.groupingBy(Attendance::getEmployee));

        List<LateEarlyTrendDto> result = new ArrayList<>();
        for (Map.Entry<Employee, List<Attendance>> entry : groupedByEmp.entrySet()) {
            Employee emp = entry.getKey();
            List<Attendance> empRecords = entry.getValue();

            long lateCount = empRecords.stream().filter(a -> Boolean.TRUE.equals(a.getLateFlag())).count();
            long earlyCount = empRecords.stream().filter(a -> Boolean.TRUE.equals(a.getEarlyDeparture())).count();

            if (lateCount > 0 || earlyCount > 0) {
                String fullName = emp.getFirstName() + (emp.getLastName() != null ? " " + emp.getLastName() : "");
                result.add(LateEarlyTrendDto.builder()
                        .department(emp.getDepartment() != null ? emp.getDepartment() : "General")
                        .employeeName(fullName)
                        .lateCount(lateCount)
                        .earlyDepartureCount(earlyCount)
                        .build());
            }
        }
        return result;
    }

    @Override
    public List<LeaveSummaryDto> getLeaveSummary() {
        List<LeaveRequest> allRequests = leaveRequestRepository.findAll();
        Map<String, List<LeaveRequest>> groupedByType = allRequests.stream()
                .collect(Collectors.groupingBy(r -> r.getLeaveType().getLeaveName()));

        List<LeaveSummaryDto> result = new ArrayList<>();
        leaveTypeRepository.findAll().forEach(lt -> {
            List<LeaveRequest> list = groupedByType.getOrDefault(lt.getLeaveName(), Collections.emptyList());
            long total = list.size();
            long approved = list.stream().filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus())).count();
            long pending = list.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
            long rejected = list.stream().filter(r -> "REJECTED".equalsIgnoreCase(r.getStatus())).count();

            long daysTaken = list.stream()
                    .filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus()))
                    .mapToLong(r -> ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()) + 1)
                    .sum();

            result.add(LeaveSummaryDto.builder()
                    .leaveTypeName(lt.getLeaveName())
                    .totalRequests(total)
                    .approvedRequests(approved)
                    .pendingRequests(pending)
                    .rejectedRequests(rejected)
                    .totalDaysTaken(daysTaken)
                    .build());
        });
        return result;
    }

    @Override
    public List<OvertimeSummaryDto> getOvertimeSummary() {
        List<Overtime> allOt = overtimeRepository.findAll();
        Map<Employee, List<Overtime>> groupedByEmp = allOt.stream()
                .collect(Collectors.groupingBy(Overtime::getEmployee));

        List<OvertimeSummaryDto> result = new ArrayList<>();
        for (Map.Entry<Employee, List<Overtime>> entry : groupedByEmp.entrySet()) {
            Employee emp = entry.getKey();
            List<Overtime> list = entry.getValue();

            long totalReq = list.size();
            BigDecimal approvedHours = list.stream()
                    .filter(o -> "APPROVED".equalsIgnoreCase(o.getStatus()) && o.getHours() != null)
                    .map(Overtime::getHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String fullName = emp.getFirstName() + (emp.getLastName() != null ? " " + emp.getLastName() : "");
            result.add(OvertimeSummaryDto.builder()
                    .employeeId(emp.getEmployeeId())
                    .employeeName(fullName)
                    .department(emp.getDepartment() != null ? emp.getDepartment() : "General")
                    .totalRequests(totalReq)
                    .approvedHours(approvedHours)
                    .build());
        }
        return result;
    }
}
