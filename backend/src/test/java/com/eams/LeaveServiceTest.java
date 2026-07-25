package com.eams;

import com.eams.dto.LeaveRequestRequest;
import com.eams.dto.LeaveRequestResponse;
import com.eams.entity.Employee;
import com.eams.entity.LeaveRequest;
import com.eams.entity.LeaveType;
import com.eams.repository.EmployeeRepository;
import com.eams.repository.LeaveRequestRepository;
import com.eams.repository.LeaveTypeRepository;
import com.eams.service.impl.LeaveRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    @InjectMocks
    private LeaveRequestServiceImpl leaveRequestService;

    private Employee employee;
    private LeaveType leaveType;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setEmployeeId(101);

        leaveType = new LeaveType();
        leaveType.setLeaveTypeId(1);
        leaveType.setLeaveName("Annual Leave");
        leaveType.setMaxDays(14);
    }

    @Test
    public void testApplyLeave_Success() {
        LeaveRequestRequest request = new LeaveRequestRequest();
        request.setEmployeeId(101);
        request.setLeaveTypeId(1);
        request.setStartDate(LocalDate.of(2026, 8, 1));
        request.setEndDate(LocalDate.of(2026, 8, 5));
        request.setReason("Vacation");

        when(employeeRepository.findById(101)).thenReturn(Optional.of(employee));
        when(leaveTypeRepository.findById(1)).thenReturn(Optional.of(leaveType));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(invocation -> {
            LeaveRequest lr = invocation.getArgument(0);
            lr.setRequestId(10);
            return lr;
        });

        LeaveRequestResponse response = leaveRequestService.applyLeave(request);

        assertNotNull(response);
        assertEquals(10, response.getRequestId());
        assertEquals("PENDING", response.getStatus());
        assertEquals("Annual Leave", response.getLeaveTypeName());
    }

    @Test
    public void testApproveLeave_Success() {
        LeaveRequest request = new LeaveRequest();
        request.setRequestId(10);
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStatus("PENDING");

        when(leaveRequestRepository.findById(10)).thenReturn(Optional.of(request));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LeaveRequestResponse response = leaveRequestService.approveOrReject(10, "APPROVED");

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
    }
}
