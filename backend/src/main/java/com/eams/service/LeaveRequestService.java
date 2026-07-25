package com.eams.service;

import com.eams.dto.LeaveRequestRequest;
import com.eams.dto.LeaveRequestResponse;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequestResponse applyLeave(LeaveRequestRequest request);
    List<LeaveRequestResponse> getEmployeeLeaves(Integer employeeId);
    List<LeaveRequestResponse> getAllLeaves();
    LeaveRequestResponse approveOrReject(Integer requestId, String status);
}
