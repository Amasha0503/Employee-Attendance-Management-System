package com.eams.controller;

import com.eams.dto.OvertimeRequest;
import com.eams.dto.OvertimeResponse;
import com.eams.service.OvertimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/overtime")
@RequiredArgsConstructor
public class OvertimeController {

    private final OvertimeService overtimeService;

    @PostMapping
    public ResponseEntity<OvertimeResponse> requestOvertime(@Valid @RequestBody OvertimeRequest request) {
        return ResponseEntity.ok(overtimeService.requestOvertime(request));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<OvertimeResponse>> getEmployeeOvertime(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(overtimeService.getEmployeeOvertime(employeeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OvertimeResponse>> getAllOvertime() {
        return ResponseEntity.ok(overtimeService.getAllOvertime());
    }

    @PutMapping("/{overtimeId}")
    public ResponseEntity<OvertimeResponse> updateStatus(@PathVariable Integer overtimeId, @RequestParam String status) {
        return ResponseEntity.ok(overtimeService.approveOrReject(overtimeId, status));
    }
}
