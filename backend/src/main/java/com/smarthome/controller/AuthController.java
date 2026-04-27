package com.smarthome.controller;

import com.smarthome.dto.LoginRequestDto;
import com.smarthome.dto.UserRegistrationDto;
import com.smarthome.entity.User;
import com.smarthome.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        System.out.println("=== LOGIN DEBUG START ===");
        System.out.println("Received login data: " + loginRequest);
        
        try {
            // Validate required fields
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
            }
            
            // Authenticate user
            Map<String, Object> response = authService.authenticate(
                loginRequest.getEmail().trim().toLowerCase(), 
                loginRequest.getPassword()
            );
            
            System.out.println("Authentication successful! Response: " + response);
            System.out.println("=== LOGIN DEBUG END ===");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }
    
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        System.out.println("=== REGISTRATION DEBUG START ===");
        System.out.println("Received registration data: " + registrationDto);
        
        try {
            // Validate required fields
            if (registrationDto.getFullName() == null || registrationDto.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Full name is required"));
            }
            if (registrationDto.getEmail() == null || registrationDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (registrationDto.getPassword() == null || registrationDto.getPassword().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
            }
            
            // Create User entity
            User user = new User();
            user.setFullName(registrationDto.getFullName().trim());
            user.setEmail(registrationDto.getEmail().trim().toLowerCase());
            user.setPassword(registrationDto.getPassword());
            
            User registeredUser = authService.registerUser(user);
            System.out.println("Registration successful! User ID: " + registeredUser.getId());
            
            System.out.println("=== REGISTRATION DEBUG END ===");
            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", registeredUser.getId(),
                "email", registeredUser.getEmail()
            ));
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "Auth service is running"));
    }
}
