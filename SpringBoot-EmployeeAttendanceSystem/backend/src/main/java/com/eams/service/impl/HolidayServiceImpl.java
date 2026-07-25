package com.eams.service.impl;

import com.eams.dto.HolidayRequest;
import com.eams.dto.HolidayResponse;
import com.eams.entity.Holiday;
import com.eams.repository.HolidayRepository;
import com.eams.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    @Transactional
    public HolidayResponse create(HolidayRequest request) {
        Holiday holiday = new Holiday();
        applyChanges(holiday, request);
        return mapToResponse(holidayRepository.save(holiday));
    }

    @Override
    @Transactional
    public HolidayResponse update(Integer holidayId, HolidayRequest request) {
        Holiday holiday = holidayRepository.findById(holidayId).orElseThrow(() -> new IllegalArgumentException("Holiday not found"));
        applyChanges(holiday, request);
        return mapToResponse(holidayRepository.save(holiday));
    }

    @Override
    @Transactional
    public void delete(Integer holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId).orElseThrow(() -> new IllegalArgumentException("Holiday not found"));
        holidayRepository.delete(holiday);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponse> findAll() {
        return holidayRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HolidayResponse findById(Integer holidayId) {
        return mapToResponse(holidayRepository.findById(holidayId).orElseThrow(() -> new IllegalArgumentException("Holiday not found")));
    }

    private void applyChanges(Holiday holiday, HolidayRequest request) {
        holiday.setHolidayName(request.getHolidayName());
        holiday.setHolidayDate(request.getHolidayDate());
    }

    private HolidayResponse mapToResponse(Holiday holiday) {
        return new HolidayResponse(holiday.getHolidayId(), holiday.getHolidayName(), holiday.getHolidayDate());
    }
}
