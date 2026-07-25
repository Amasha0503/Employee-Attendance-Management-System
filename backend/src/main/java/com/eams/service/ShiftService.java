package com.eams.service;

import com.eams.dto.ShiftRequest;
import com.eams.dto.ShiftResponse;

import java.util.List;

public interface ShiftService {
    ShiftResponse create(ShiftRequest request);
    ShiftResponse update(Integer shiftId, ShiftRequest request);
    void delete(Integer shiftId);
    List<ShiftResponse> findAll();
    ShiftResponse findById(Integer shiftId);
}
