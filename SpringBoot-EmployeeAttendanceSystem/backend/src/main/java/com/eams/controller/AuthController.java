package com.eams.controller;

import com.eams.dto.AuthRequest;
import com.eams.dto.AuthResponse;
import com.eams.entity.Role;
import com.eams.entity.User;
import com.eams.repository.RoleRepository;
import com.eams.repository.UserRepository;
import com.eams.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        String role = user.getRole() != null ? user.getRole().getRoleName() : "EMPLOYEE";
        String token = jwtUtil.generateToken(user.getUsername(), role);

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), role));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Role role = roleRepository.findByRoleName("EMPLOYEE")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("EMPLOYEE");
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), role.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, user.getUsername(), role.getRoleName()));
    }
}
