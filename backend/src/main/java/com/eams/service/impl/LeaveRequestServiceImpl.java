package com.eams.service.impl;

import com.eams.dto.LeaveRequestRequest;
import com.eams.dto.LeaveRequestResponse;
import com.eams.entity.Employee;
import com.eams.entity.LeaveRequest;
import com.eams.entity.LeaveType;
import com.eams.repository.EmployeeRepository;
import com.eams.repository.LeaveRequestRepository;
import com.eams.repository.LeaveTypeRepository;
import com.eams.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public LeaveRequestResponse applyLeave(LeaveRequestRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId()).orElseThrow(() -> new IllegalArgumentException("Leave type not found"));

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setAppliedDate(LocalDate.now());
        leaveRequest.setStatus("PENDING");
        return mapToResponse(leaveRequestRepository.save(leaveRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getEmployeeLeaves(Integer employeeId) {
        return leaveRequestRepository.findByEmployeeEmployeeId(employeeId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getAllLeaves() {
        return leaveRequestRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LeaveRequestResponse approveOrReject(Integer requestId, String status) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId).orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        leaveRequest.setStatus(status);
        return mapToResponse(leaveRequestRepository.save(leaveRequest));
    }

    private LeaveRequestResponse mapToResponse(LeaveRequest leaveRequest) {
        return new LeaveRequestResponse(
                leaveRequest.getRequestId(),
                leaveRequest.getEmployee() != null ? leaveRequest.getEmployee().getEmployeeId() : null,
                leaveRequest.getLeaveType() != null ? leaveRequest.getLeaveType().getLeaveTypeId() : null,
                leaveRequest.getLeaveType() != null ? leaveRequest.getLeaveType().getLeaveName() : null,
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getAppliedDate()
        );
    }
}
