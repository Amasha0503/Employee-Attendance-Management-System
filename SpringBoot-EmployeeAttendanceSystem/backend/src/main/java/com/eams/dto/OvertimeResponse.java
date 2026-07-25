package com.eams.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OvertimeResponse {
    private Integer overtimeId;
    private Integer employeeId;
    private LocalDate otDate;
    private BigDecimal hours;
    private String reason;
    private String status;

    public OvertimeResponse() {}

    public OvertimeResponse(Integer overtimeId, Integer employeeId, LocalDate otDate, BigDecimal hours, String reason, String status) {
        this.overtimeId = overtimeId;
        this.employeeId = employeeId;
        this.otDate = otDate;
        this.hours = hours;
        this.reason = reason;
        this.status = status;
    }

    public Integer getOvertimeId() { return overtimeId; }
    public void setOvertimeId(Integer overtimeId) { this.overtimeId = overtimeId; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public LocalDate getOtDate() { return otDate; }
    public void setOtDate(LocalDate otDate) { this.otDate = otDate; }

    public BigDecimal getHours() { return hours; }
    public void setHours(BigDecimal hours) { this.hours = hours; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
