package com.eams.service.impl;

import com.eams.dto.ShiftRequest;
import com.eams.dto.ShiftResponse;
import com.eams.entity.Shift;
import com.eams.repository.ShiftRepository;
import com.eams.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    @Override
    @Transactional
    public ShiftResponse create(ShiftRequest request) {
        Shift shift = new Shift();
        applyChanges(shift, request);
        return mapToResponse(shiftRepository.save(shift));
    }

    @Override
    @Transactional
    public ShiftResponse update(Integer shiftId, ShiftRequest request) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        applyChanges(shift, request);
        return mapToResponse(shiftRepository.save(shift));
    }

    @Override
    @Transactional
    public void delete(Integer shiftId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        shiftRepository.delete(shift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftResponse> findAll() {
        return shiftRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftResponse findById(Integer shiftId) {
        return mapToResponse(shiftRepository.findById(shiftId).orElseThrow(() -> new IllegalArgumentException("Shift not found")));
    }

    private void applyChanges(Shift shift, ShiftRequest request) {
        shift.setShiftName(request.getShiftName());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setGraceMinutes(request.getGraceMinutes() != null ? request.getGraceMinutes() : 10);
    }

    private ShiftResponse mapToResponse(Shift shift) {
        return new ShiftResponse(shift.getShiftId(), shift.getShiftName(), shift.getStartTime(), shift.getEndTime(), shift.getGraceMinutes());
    }
}
