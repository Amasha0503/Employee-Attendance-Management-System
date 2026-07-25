package com.eams.dto;

public class EmployeeResponse {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private Integer shiftId;
    private String shiftName;
    private String status;

    public EmployeeResponse() {}

    public EmployeeResponse(Integer employeeId, String firstName, String lastName, String email,
                            String phone, String department, Integer shiftId, String shiftName, String status) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.status = status;
    }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getShiftId() { return shiftId; }
    public void setShiftId(Integer shiftId) { this.shiftId = shiftId; }

    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
