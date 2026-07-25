package com.eams.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leave_types")
@Getter
@Setter
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_type_id")
    private Integer leaveTypeId;

    @NotBlank
    @Column(name = "leave_name", nullable = false, length = 50)
    private String leaveName;

    @Column(name = "max_days")
    private Integer maxDays = 0;

    @OneToMany(mappedBy = "leaveType")
    private List<LeaveRequest> leaveRequests = new ArrayList<>();
}
