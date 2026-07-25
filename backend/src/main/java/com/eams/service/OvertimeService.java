package com.eams.service;

import com.eams.dto.OvertimeRequest;
import com.eams.dto.OvertimeResponse;

import java.util.List;

public interface OvertimeService {
    OvertimeResponse requestOvertime(OvertimeRequest request);
    List<OvertimeResponse> getEmployeeOvertime(Integer employeeId);
    List<OvertimeResponse> getAllOvertime();
    OvertimeResponse approveOrReject(Integer overtimeId, String status);
}
