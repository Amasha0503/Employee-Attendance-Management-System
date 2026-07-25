package com.eams.repository;

import com.eams.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByEmployeeEmployeeIdAndAttendanceDate(Integer employeeId, LocalDate attendanceDate);
    List<Attendance> findByEmployeeEmployeeIdOrderByAttendanceDateDesc(Integer employeeId);
    List<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);
    List<Attendance> findByEmployeeEmployeeIdAndAttendanceDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);
}
