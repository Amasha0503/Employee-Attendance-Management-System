package com.eams.repository;

import com.eams.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByEmployeeEmployeeId(Integer employeeId);
    List<LeaveRequest> findByEmployeeEmployeeIdAndStartDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByStatus(String status);
}
