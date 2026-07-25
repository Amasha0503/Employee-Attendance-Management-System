package com.eams.config;

import com.eams.entity.Employee;
import com.eams.entity.Role;
import com.eams.entity.User;
import com.eams.repository.EmployeeRepository;
import com.eams.repository.RoleRepository;
import com.eams.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DATA INITIALIZER RUNNING ===");

        // Ensure ADMIN role exists
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName("ADMIN");
                    return roleRepository.save(r);
                });

        // Ensure EMPLOYEE role exists
        Role employeeRole = roleRepository.findByRoleName("EMPLOYEE")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName("EMPLOYEE");
                    return roleRepository.save(r);
                });

        // Seed or update Admin User
        User adminUser = userRepository.findByUsername("admin").orElseGet(User::new);
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(adminRole);
        adminUser.setActive(true);
        userRepository.save(adminUser);
        System.out.println("=== ADMIN USER SEEDED / UPDATED ===");

        // Seed or update Demo Employee User & Profile
        User empUser = userRepository.findByUsername("john_doe").orElseGet(User::new);
        empUser.setUsername("john_doe");
        empUser.setPassword(passwordEncoder.encode("user123"));
        empUser.setRole(employeeRole);
        empUser.setActive(true);
        User savedUser = userRepository.save(empUser);
        System.out.println("=== JOHN_DOE USER SEEDED / UPDATED ===");

        if (employeeRepository.findAll().isEmpty()) {
            Employee emp = new Employee();
            emp.setUser(savedUser);
            emp.setFirstName("John");
            emp.setLastName("Doe");
            emp.setEmail("john.doe@company.com");
            emp.setPhone("+1234567890");
            emp.setDepartment("Engineering");
            emp.setStatus("ACTIVE");
            employeeRepository.save(emp);
            System.out.println("=== JOHN_DOE PROFILE SEEDED ===");
        }
    }
}
