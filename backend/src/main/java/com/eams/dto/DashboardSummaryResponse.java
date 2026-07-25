package com.eams.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryResponse {
    private long totalEmployees;
    private long presentToday;
    private long lateToday;
    private long absentToday;
    private long pendingLeaves;
    private long pendingOvertime;
}
