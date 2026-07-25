# Employee Attendance Management System

Full-stack academic project for the iCET Enterprise Engineering module.

## Overview

This system provides employee attendance tracking, authentication, shift management, leave requests, overtime requests, dashboards, and report exports using a Spring Boot backend, Angular frontend, and an existing MySQL database schema.

## Tech Stack

- Backend: Spring Boot 3, Spring Web, Spring Data JPA, Spring Security, Validation, JWT, MySQL Connector/J, Maven
- Frontend: Angular 17+, Angular Material, Angular Router, Reactive Forms, Chart.js via ng2-charts
- Database: MySQL (`employee_attendance_db`)

## Project Structure

- `backend/` - Spring Boot REST API
- `frontend/` - Angular SPA

## Run Locally

### Backend

1. Configure database and JWT values in `backend/src/main/resources/application.properties`.
2. Run the backend on port `8080`:

```bash
cd backend
mvn spring-boot:run
```

### Frontend

1. Install Node dependencies in `frontend/`.
2. Run the Angular app on port `4200`:

```bash
cd frontend
npm install
npm start
```

## Notes

- The database schema already exists, so JPA is configured with `ddl-auto=validate`.
- Angular is scaffolded with standalone components and routing.