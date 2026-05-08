package com.smarthome.controller;

import com.smarthome.entity.Device;
import com.smarthome.entity.DeviceStatus;
import com.smarthome.entity.DeviceType;
import com.smarthome.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/mobile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MobileController {

    @Autowired
    private DeviceService deviceService;

    // Simple login endpoint for mobile (no auth required for demo)
    @PostMapping("/login")
    public ResponseEntity<?> mobileLogin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        // Simple validation for demo
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password required"));
        }
        
        // For demo purposes, accept any email/password and return a dummy token
        String dummyToken = "mobile_token_" + System.currentTimeMillis();
        
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "token", dummyToken,
            "userId", "demo_user_" + email.hashCode(),
            "email", email
        ));
    }

    // Get all devices for mobile dashboard
    @GetMapping("/devices")
    public ResponseEntity<?> getMobileDevices() {
        try {
            // Return sample devices for demo
            List<Map<String, Object>> devices = Arrays.asList(
                createSampleDevice("1", "Living Room Light", "LIGHT", "ON", 75),
                createSampleDevice("2", "Kitchen Temperature", "TEMPERATURE", "ON", 22),
                createSampleDevice("3", "Front Door Camera", "CAMERA", "ON", 100),
                createSampleDevice("4", "Smart TV", "ENTERTAINMENT", "OFF", 0),
                createSampleDevice("5", "Bedroom AC", "CLIMATE", "ON", 65)
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "devices", devices,
                "total", devices.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "devices", Collections.emptyList(),
                "total", 0
            ));
        }
    }

    // Get device by ID
    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<?> getMobileDevice(@PathVariable String deviceId) {
        Map<String, Object> device = createSampleDevice(deviceId, "Sample Device", "LIGHT", "ON", 75);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "device", device
        ));
    }

    // Update device status
    @PutMapping("/devices/{deviceId}/status")
    public ResponseEntity<?> updateDeviceStatus(@PathVariable String deviceId, @RequestBody Map<String, String> status) {
        String newStatus = status.get("status");
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Device status updated",
            "deviceId", deviceId,
            "newStatus", newStatus,
            "timestamp", System.currentTimeMillis()
        ));
    }

    // Get energy data for mobile
    @GetMapping("/energy")
    public ResponseEntity<?> getEnergyData() {
        Map<String, Object> energyData = new HashMap<>();
        energyData.put("todayUsage", 15.6);
        energyData.put("todayCost", 3.95);
        energyData.put("monthlyUsage", 342.8);
        energyData.put("monthlyCost", 87.45);
        energyData.put("devices", Arrays.asList(
            Map.of("name", "HVAC System", "usage", 45.2, "percentage", 38),
            Map.of("name", "Lighting", "usage", 22.1, "percentage", 19),
            Map.of("name", "Kitchen", "usage", 18.5, "percentage", 15)
        ));
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", energyData
        ));
    }

    // Get security events for mobile
    @GetMapping("/security")
    public ResponseEntity<?> getSecurityEvents() {
        List<Map<String, Object>> events = Arrays.asList(
            Map.of("time", "2:30 AM", "type", "motion", "location", "Front Door", "severity", "low"),
            Map.of("time", "6:45 AM", "type", "door_open", "location", "Garage", "severity", "normal"),
            Map.of("time", "8:15 AM", "type", "motion", "location", "Backyard", "severity", "low"),
            Map.of("time", "12:30 PM", "type", "package", "location", "Front Door", "severity", "normal")
        );
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "events", events,
            "total", events.size(),
            "riskLevel", "low"
        ));
    }

    // Get automations for mobile
    @GetMapping("/automations")
    public ResponseEntity<?> getAutomations() {
        List<Map<String, Object>> automations = Arrays.asList(
            Map.of("id", "1", "name", "Morning Routine", "active", true, "nextRun", "6:00 AM"),
            Map.of("id", "2", "name", "Night Security", "active", true, "nextRun", "10:00 PM"),
            Map.of("id", "3", "name", "Energy Saver", "active", false, "nextRun", "-")
        );
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "automations", automations,
            "total", automations.size()
        ));
    }

    // Get user profile for mobile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("name", "Demo User");
        profile.put("email", "demo@smarthome.com");
        profile.put("phone", "+1234567890");
        profile.put("address", "123 Smart Home Street");
        profile.put("memberSince", "2024-01-01");
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "profile", profile
        ));
    }

    // Get alerts for mobile
    @GetMapping("/alerts")
    public ResponseEntity<?> getAlerts() {
        List<Map<String, Object>> alerts = Arrays.asList(
            Map.of("id", "1", "type", "info", "message", "System update available", "time", "2 hours ago"),
            Map.of("id", "2", "type", "warning", "message", "High energy usage detected", "time", "5 hours ago"),
            Map.of("id", "3", "type", "success", "message", "All devices online", "time", "1 day ago")
        );
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "alerts", alerts,
            "total", alerts.size()
        ));
    }

    // Add new device (mobile)
    @PostMapping("/devices")
    public ResponseEntity<?> addDevice(@RequestBody Map<String, Object> deviceData) {
        String name = (String) deviceData.get("name");
        String type = (String) deviceData.get("type");
        
        String deviceId = "device_" + System.currentTimeMillis();
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Device added successfully",
            "device", createSampleDevice(deviceId, name, type, "OFF", 0)
        ));
    }

    // Delete device (mobile)
    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Device deleted successfully",
            "deviceId", deviceId
        ));
    }

    // Helper method to create sample devices
    private Map<String, Object> createSampleDevice(String id, String name, String type, String status, int value) {
        Map<String, Object> device = new HashMap<>();
        device.put("id", id);
        device.put("name", name);
        device.put("type", type);
        device.put("status", status);
        device.put("value", value);
        device.put("room", "Living Room");
        device.put("lastUpdated", System.currentTimeMillis());
        device.put("favorite", false);
        
        // Add type-specific properties
        switch (type) {
            case "LIGHT":
                device.put("brightness", value);
                device.put("icon", "lightbulb");
                break;
            case "TEMPERATURE":
                device.put("temperature", value);
                device.put("icon", "thermometer");
                break;
            case "CAMERA":
                device.put("recording", status.equals("ON"));
                device.put("icon", "camera");
                break;
            case "CLIMATE":
                device.put("targetTemp", value);
                device.put("icon", "wind");
                break;
            default:
                device.put("icon", "device");
        }
        
        return device;
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "service", "Smart Home Mobile API",
            "timestamp", System.currentTimeMillis(),
            "version", "1.0.0"
        ));
    }
}
