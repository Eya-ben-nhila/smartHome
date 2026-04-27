package com.smarthome.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v2/auth")
public class AuthControllerV2 {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> data) {
        System.out.println("V2 Registration called with: " + data);
        
        try {
            String fullName = (String) data.get("fullName");
            String email = (String) data.get("email");
            String password = (String) data.get("password");
            
            System.out.println("Extracted - fullName: " + fullName + ", email: " + email + ", password: " + password);
            
            return ResponseEntity.ok(Map.of(
                "message", "V2 Registration successful",
                "received", Map.of("fullName", fullName, "email", email)
            ));
        } catch (Exception e) {
            System.out.println("V2 Registration error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
