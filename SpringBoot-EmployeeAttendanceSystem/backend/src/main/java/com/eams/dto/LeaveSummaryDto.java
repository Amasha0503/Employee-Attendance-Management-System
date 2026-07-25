package com.eams.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveSummaryDto {
    private String leaveTypeName;
    private long totalRequests;
    private long approvedRequests;
    private long pendingRequests;
    private long rejectedRequests;
    private long totalDaysTaken;
}
