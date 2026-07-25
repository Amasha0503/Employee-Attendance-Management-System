# Employee Attendance Management System — iCET Academic Project

A full-stack enterprise web application built for the **iCET Enterprise Engineering Module** to manage employee attendance, shift schedules, leave applications, overtime tracking, analytics dashboards, and report exports.

---

## Tech Stack & Architecture

### Backend
- **Framework**: Spring Boot 3.3.2 (Java 17)
- **Security**: Spring Security 6 with JWT (`io.jsonwebtoken:jjwt-api:0.11.5`) & BCrypt Password Encoding
- **Database Access**: Spring Data JPA / Hibernate (mapped to MySQL database `employee_attendance_db` with `hibernate.ddl-auto=validate`)
- **PDF & CSV Exporters**: OpenPDF (`com.github.librepdf:openpdf:1.3.30`) & Java Stream CSV Writers
- **Validation & Exception Handling**: Bean Validation (`@NotNull`, `@Email`) & Global `@ControllerAdvice` Exception Handling
- **Build Tool**: Maven

### Frontend
- **Framework**: Angular 17 (Standalone Component Architecture)
- **UI Library**: Angular Material 17 (`@angular/material`)
- **State & Communication**: RxJS, Angular HTTP Client Interceptors (Auth Bearer Token & Global Loading Spinner)
- **Charts & Visualizations**: Chart.js (`chart.js`) & `ng2-charts`
- **Routing & Protection**: Angular Router with `AuthGuard` & `RoleGuard`

---

## Implemented Features

1. **User Authentication & Role-Based Access**
   - JWT-based authentication for `ADMIN` and `EMPLOYEE` roles.
   - Secure login, password encryption, and route protection.

2. **Attendance Management**
   - Employee check-in & check-out with automatic timestamp recording.
   - Automatic detection of **Late Arrivals** (based on shift start time + grace minutes) and **Early Departures** (based on shift end time).
   - Personal attendance history and organization-wide attendance view.

3. **Shift & Schedule Management**
   - Admin CRUD operations for work shifts and company holidays.

4. **Leave Management**
   - Employee leave requests against leave types (`max_days`).
   - Admin approval/rejection workflow and leave balance tracking.

5. **Overtime Tracking**
   - Overtime application and approval workflow with total hours aggregation.

6. **Analytics Dashboards & Interactive Charts**
   - Real-time stat metrics (Total Active Employees, Present Today, Late Arrivals, Pending Requests).
   - Interactive 30-Day Attendance Trend chart (Present / Late / Absent breakdown).

7. **Report Exports (CSV & PDF)**
   - **Daily Attendance Sheet** (CSV & PDF)
   - **Monthly Attendance Summary** (CSV & PDF)
   - **Leave Utilization Report** (CSV & PDF)
   - **Overtime Summary Report** (CSV & PDF)
   - **Late / Early Trend Analysis** (CSV & PDF)

8. **Global Error Handling & Unit Testing**
   - Global `@ControllerAdvice` returning standardized `ErrorResponse` objects.
   - JUnit 5 & Mockito test suite for late/early attendance logic and leave balance calculations.

---

## REST API Endpoints Overview

| Category | Endpoint | Method | Role | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `/api/v1/auth/login` | `POST` | Public | Authenticate user & return JWT |
| **Attendance** | `/api/v1/attendance/check-in` | `POST` | Employee/Admin | Employee check-in |
| **Attendance** | `/api/v1/attendance/check-out` | `POST` | Employee/Admin | Employee check-out |
| **Attendance** | `/api/v1/attendance/employee/{id}` | `GET` | Employee/Admin | Get employee attendance history |
| **Leave** | `/api/v1/leave/apply` | `POST` | Employee/Admin | Submit leave request |
| **Leave** | `/api/v1/leave/approve/{id}` | `PUT` | Admin | Approve or reject leave |
| **Overtime** | `/api/v1/overtime/apply` | `POST` | Employee/Admin | Submit overtime request |
| **Dashboard** | `/api/v1/dashboard/summary` | `GET` | Employee/Admin | Get dashboard summary metrics |
| **Dashboard** | `/api/v1/dashboard/attendance-trend` | `GET` | Employee/Admin | Attendance trend dataset |
| **Reports** | `/api/v1/reports/export/csv/{type}` | `GET` | Admin | Export report in CSV format |
| **Reports** | `/api/v1/reports/export/pdf/{type}` | `GET` | Admin | Export report in PDF format |

---

## How to Run Locally

### 1. Database Setup
Ensure MySQL is running on `localhost:3306` with the `employee_attendance_db` schema imported. Update DB credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_attendance_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### 2. Run Backend
```bash
cd backend
mvn clean test   # Run unit test suite
mvn spring-boot:run
```
The REST API will start on `http://localhost:8080`.

### 3. Run Frontend
```bash
cd frontend
npm install
npm start
```
The Angular application will run on `http://localhost:4200`.

---

## Suggested Git Conventional Commits History
1. `chore: initial project scaffolding for backend and frontend`
2. `feat(backend): add JPA entities and repositories for core schema`
3. `feat: implement JWT authentication (backend + frontend)`
4. `feat: admin CRUD for employees, shifts, and holidays`
5. `feat: attendance check-in/check-out with late and early-departure detection`
6. `feat: leave application, approval workflow, and balance tracking`
7. `feat: overtime request and approval workflow`
8. `feat: dashboards with charts and CSV/PDF report exports`
9. `chore: error handling, tests, UI polish, and final README update`