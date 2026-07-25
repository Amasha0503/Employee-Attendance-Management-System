package com.eams.controller;

import com.eams.dto.EmployeeRequest;
import com.eams.dto.EmployeeResponse;
import com.eams.dto.HolidayRequest;
import com.eams.dto.HolidayResponse;
import com.eams.dto.ShiftRequest;
import com.eams.dto.ShiftResponse;
import com.eams.service.EmployeeService;
import com.eams.service.HolidayService;
import com.eams.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final HolidayService holidayService;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Integer employeeId, @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.update(employeeId, request));
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Integer employeeId) {
        employeeService.deactivate(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shifts")
    public ResponseEntity<List<ShiftResponse>> getShifts() {
        return ResponseEntity.ok(shiftService.findAll());
    }

    @PostMapping("/shifts")
    public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody ShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.create(request));
    }

    @PutMapping("/shifts/{shiftId}")
    public ResponseEntity<ShiftResponse> updateShift(@PathVariable Integer shiftId, @Valid @RequestBody ShiftRequest request) {
        return ResponseEntity.ok(shiftService.update(shiftId, request));
    }

    @DeleteMapping("/shifts/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable Integer shiftId) {
        shiftService.delete(shiftId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<HolidayResponse>> getHolidays() {
        return ResponseEntity.ok(holidayService.findAll());
    }

    @PostMapping("/holidays")
    public ResponseEntity<HolidayResponse> createHoliday(@Valid @RequestBody HolidayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(holidayService.create(request));
    }

    @PutMapping("/holidays/{holidayId}")
    public ResponseEntity<HolidayResponse> updateHoliday(@PathVariable Integer holidayId, @Valid @RequestBody HolidayRequest request) {
        return ResponseEntity.ok(holidayService.update(holidayId, request));
    }

    @DeleteMapping("/holidays/{holidayId}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Integer holidayId) {
        holidayService.delete(holidayId);
        return ResponseEntity.noContent().build();
    }
}
