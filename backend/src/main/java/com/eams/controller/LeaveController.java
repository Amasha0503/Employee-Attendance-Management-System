package com.eams.controller;

import com.eams.dto.LeaveRequestRequest;
import com.eams.dto.LeaveRequestResponse;
import com.eams.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> applyLeave(@Valid @RequestBody LeaveRequestRequest request) {
        return ResponseEntity.ok(leaveRequestService.applyLeave(request));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestResponse>> getEmployeeLeaves(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(leaveRequestService.getEmployeeLeaves(employeeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LeaveRequestResponse>> getAllLeaves() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaves());
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<LeaveRequestResponse> updateStatus(@PathVariable Integer requestId, @RequestParam String status) {
        return ResponseEntity.ok(leaveRequestService.approveOrReject(requestId, status));
    }
}
