package com.eams.service;

import com.eams.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse getDashboardSummary();
    List<AttendanceTrendDto> getAttendanceTrend(LocalDate startDate, LocalDate endDate);
    List<LateEarlyTrendDto> getLateEarlyTrend(LocalDate startDate, LocalDate endDate);
    List<LeaveSummaryDto> getLeaveSummary();
    List<OvertimeSummaryDto> getOvertimeSummary();
}
