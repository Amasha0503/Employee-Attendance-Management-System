package com.eams.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceTrendDto {
    private LocalDate date;
    private long presentCount;
    private long lateCount;
    private long absentCount;
}
