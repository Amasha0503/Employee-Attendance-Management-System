package com.eams.service.impl;

import com.eams.dto.EmployeeRequest;
import com.eams.dto.EmployeeResponse;
import com.eams.entity.Employee;
import com.eams.entity.Shift;
import com.eams.repository.EmployeeRepository;
import com.eams.repository.ShiftRepository;
import com.eams.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = new Employee();
        applyChanges(employee, request);
        employee.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");
        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse update(Integer employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        applyChanges(employee, request);
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }
        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse deactivate(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        employee.setStatus("INACTIVE");
        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Integer employeeId) {
        return mapToResponse(employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found")));
    }

    private void applyChanges(Employee employee, EmployeeRequest request) {
        employee.setEmployeeId(request.getEmployeeId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());

        if (request.getShiftId() != null) {
            Shift shift = shiftRepository.findById(request.getShiftId()).orElseThrow(() -> new IllegalArgumentException("Shift not found"));
            employee.setShift(shift);
        }
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartment(),
                null,
                employee.getShift() != null ? employee.getShift().getShiftId() : null,
                employee.getShift() != null ? employee.getShift().getShiftName() : null,
                employee.getStatus()
        );
    }
}
