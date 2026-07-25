package com.eams.service;

import com.eams.dto.EmployeeRequest;
import com.eams.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse update(Integer employeeId, EmployeeRequest request);
    EmployeeResponse deactivate(Integer employeeId);
    List<EmployeeResponse> findAll();
    EmployeeResponse findById(Integer employeeId);
}
