package com.eams.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OvertimeSummaryDto {
    private Integer employeeId;
    private String employeeName;
    private String department;
    private long totalRequests;
    private BigDecimal approvedHours;
}
