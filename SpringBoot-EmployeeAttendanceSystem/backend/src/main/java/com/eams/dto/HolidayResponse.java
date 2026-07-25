package com.eams.dto;

import java.time.LocalDate;

public class HolidayResponse {
    private Integer holidayId;
    private String holidayName;
    private LocalDate holidayDate;

    public HolidayResponse() {}

    public HolidayResponse(Integer holidayId, String holidayName, LocalDate holidayDate) {
        this.holidayId = holidayId;
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
    }

    public Integer getHolidayId() { return holidayId; }
    public void setHolidayId(Integer holidayId) { this.holidayId = holidayId; }

    public String getHolidayName() { return holidayName; }
    public void setHolidayName(String holidayName) { this.holidayName = holidayName; }

    public LocalDate getHolidayDate() { return holidayDate; }
    public void setHolidayDate(LocalDate holidayDate) { this.holidayDate = holidayDate; }
}
