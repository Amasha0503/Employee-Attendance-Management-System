package com.eams.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OvertimeRequest {
    @NotNull(message = "Employee id is required")
    private Integer employeeId;

    @NotNull(message = "OT date is required")
    private LocalDate otDate;

    @NotNull(message = "Hours are required")
    private BigDecimal hours;

    private String reason;

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public LocalDate getOtDate() { return otDate; }
    public void setOtDate(LocalDate otDate) { this.otDate = otDate; }

    public BigDecimal getHours() { return hours; }
    public void setHours(BigDecimal hours) { this.hours = hours; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
