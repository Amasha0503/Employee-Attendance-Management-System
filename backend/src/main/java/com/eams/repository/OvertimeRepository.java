package com.eams.repository;

import com.eams.entity.Overtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRepository extends JpaRepository<Overtime, Integer> {
    List<Overtime> findByEmployeeEmployeeId(Integer employeeId);
    List<Overtime> findByEmployeeEmployeeIdAndOtDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);
    List<Overtime> findByStatus(String status);
}
