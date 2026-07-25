package com.eams.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LeaveRequestRequest {
    @NotNull(message = "Employee id is required")
    private Integer employeeId;

    @NotNull(message = "Leave type id is required")
    private Integer leaveTypeId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String reason;

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(Integer leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
