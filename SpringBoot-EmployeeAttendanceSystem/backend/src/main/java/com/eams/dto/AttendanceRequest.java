package com.eams.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AttendanceRequest {
    @NotNull(message = "Employee id is required")
    private Integer employeeId;

    private LocalDateTime timestamp;

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
