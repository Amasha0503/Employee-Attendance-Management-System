package com.eams.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceResponse {
    private Integer attendanceId;
    private Integer employeeId;
    private LocalDate attendanceDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String status;
    private Boolean lateFlag;
    private Boolean earlyDeparture;

    public AttendanceResponse() {}

    public AttendanceResponse(Integer attendanceId, Integer employeeId, LocalDate attendanceDate, LocalDateTime checkIn,
                              LocalDateTime checkOut, String status, Boolean lateFlag, Boolean earlyDeparture) {
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.lateFlag = lateFlag;
        this.earlyDeparture = earlyDeparture;
    }

    public Integer getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Integer attendanceId) { this.attendanceId = attendanceId; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getLateFlag() { return lateFlag; }
    public void setLateFlag(Boolean lateFlag) { this.lateFlag = lateFlag; }

    public Boolean getEarlyDeparture() { return earlyDeparture; }
    public void setEarlyDeparture(Boolean earlyDeparture) { this.earlyDeparture = earlyDeparture; }
}
