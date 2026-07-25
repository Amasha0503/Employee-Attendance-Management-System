package com.eams.service.impl;

import com.eams.dto.OvertimeRequest;
import com.eams.dto.OvertimeResponse;
import com.eams.entity.Employee;
import com.eams.entity.Overtime;
import com.eams.repository.EmployeeRepository;
import com.eams.repository.OvertimeRepository;
import com.eams.service.OvertimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OvertimeServiceImpl implements OvertimeService {

    private final OvertimeRepository overtimeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public OvertimeResponse requestOvertime(OvertimeRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        Overtime overtime = new Overtime();
        overtime.setEmployee(employee);
        overtime.setOtDate(request.getOtDate());
        overtime.setHours(request.getHours());
        overtime.setReason(request.getReason());
        overtime.setStatus("PENDING");
        return mapToResponse(overtimeRepository.save(overtime));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OvertimeResponse> getEmployeeOvertime(Integer employeeId) {
        return overtimeRepository.findByEmployeeEmployeeId(employeeId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OvertimeResponse> getAllOvertime() {
        return overtimeRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OvertimeResponse approveOrReject(Integer overtimeId, String status) {
        Overtime overtime = overtimeRepository.findById(overtimeId).orElseThrow(() -> new IllegalArgumentException("Overtime request not found"));
        overtime.setStatus(status);
        return mapToResponse(overtimeRepository.save(overtime));
    }

    private OvertimeResponse mapToResponse(Overtime overtime) {
        return new OvertimeResponse(
                overtime.getOvertimeId(),
                overtime.getEmployee() != null ? overtime.getEmployee().getEmployeeId() : null,
                overtime.getOtDate(),
                overtime.getHours(),
                overtime.getReason(),
                overtime.getStatus()
        );
    }
}
