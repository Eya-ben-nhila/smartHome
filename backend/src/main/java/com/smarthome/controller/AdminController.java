package com.smarthome.controller;

import com.smarthome.dto.admin.AdminCreateUserRequest;
import com.smarthome.dto.admin.AdminUpdateUserRequest;
import com.smarthome.dto.admin.AdminUserResponse;
import com.smarthome.entity.Role;
import com.smarthome.entity.User;
import com.smarthome.repository.UserRepository;
import com.smarthome.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
            UserRepository userRepository,
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> getUsers() {
        List<AdminUserResponse> users = userRepository.findAll()
                .stream()
                .map(AdminUserResponse::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody AdminCreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        if (request.getRole() == Role.USER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Role USER is deprecated. Use EMPLOYEE or ADMIN"));
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole() == null ? Role.EMPLOYEE : request.getRole());
        user.setActive(request.isEnabled());
        user.setProfileImageUrl(request.getProfileImageUrl());

        User created = userDetailsService.createUser(user);
        return ResponseEntity.ok(AdminUserResponse.from(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody AdminUpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getRole() != null) {
            if (request.getRole() == Role.USER) {
                return ResponseEntity.badRequest().body(Map.of("error", "Role USER is deprecated. Use EMPLOYEE or ADMIN"));
            }
            user.setRole(request.getRole());
        }
        if (request.getEnabled() != null) {
            user.setActive(request.getEnabled());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getPassword().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl().isBlank() ? null : request.getProfileImageUrl().trim());
        }

        User updated = userRepository.save(user);
        return ResponseEntity.ok(AdminUserResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id, @RequestParam(defaultValue = "false") boolean hardDelete) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (hardDelete) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "User deleted"));
        }

        user.setActive(false);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User disabled"));
    }
}
