package com.eams.service;

import com.eams.dto.HolidayRequest;
import com.eams.dto.HolidayResponse;

import java.util.List;

public interface HolidayService {
    HolidayResponse create(HolidayRequest request);
    HolidayResponse update(Integer holidayId, HolidayRequest request);
    void delete(Integer holidayId);
    List<HolidayResponse> findAll();
    HolidayResponse findById(Integer holidayId);
}
