package com.smarthome.controller;

import com.smarthome.entity.Device;
import com.smarthome.entity.DeviceType;
import com.smarthome.entity.DeviceStatus;
import com.smarthome.mqtt.MqttClientService;
import com.smarthome.service.CurrentUserService;
import com.smarthome.service.DeviceService;
import com.smarthome.websocket.WebSocketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private CurrentUserService currentUserService;
    
    @Autowired
    private WebSocketService webSocketService;
    
    @Autowired
    private MqttClientService mqttClientService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Device>> getAllDevices() {
        String userId = currentUserService.getCurrentUserId();
        List<Device> devices = deviceService.getAllDevicesByUserId(userId);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
        Optional<Device> device = deviceService.getDeviceById(id, currentUserService.getCurrentUserId());
        return device.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody Device device) {
        device.setUserId(currentUserService.getCurrentUserId());
        Device createdDevice = deviceService.createDevice(device);
        // Send real-time update
        webSocketService.sendDeviceStatusUpdate(createdDevice);
        return ResponseEntity.ok(createdDevice);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Device> updateDevice(@PathVariable String id, @Valid @RequestBody Device device) {
        try {
            Device updatedDevice = deviceService.updateDevice(id, currentUserService.getCurrentUserId(), device);
            // Send real-time update
            webSocketService.sendDeviceStatusUpdate(updatedDevice);
            return ResponseEntity.ok(updatedDevice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteDevice(@PathVariable String id) {
        deviceService.deleteDevice(id, currentUserService.getCurrentUserId());
        return ResponseEntity.ok(Map.of("message", "Device deleted successfully"));
    }

    @GetMapping("/online")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Device>> getOnlineDevices() {
        String userId = currentUserService.getCurrentUserId();
        List<Device> devices = deviceService.getOnlineDevicesByUserId(userId);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/type/{deviceType}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Device>> getDevicesByType(@PathVariable DeviceType deviceType) {
        String userId = currentUserService.getCurrentUserId();
        List<Device> devices = deviceService.getDevicesByType(userId, deviceType);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Device> updateDeviceStatus(@PathVariable String id, @RequestBody Map<String, String> status) {
        try {
            DeviceStatus deviceStatus = DeviceStatus.valueOf(status.get("status"));
            Device updatedDevice = deviceService.updateDeviceStatus(id, currentUserService.getCurrentUserId(), deviceStatus);
            // Send real-time update
            webSocketService.sendDeviceStatusUpdate(updatedDevice);
            return ResponseEntity.ok(updatedDevice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDeviceStats() {
        String userId = currentUserService.getCurrentUserId();
        Long onlineCount = deviceService.countOnlineDevicesByUserId(userId);
        List<Device> allDevices = deviceService.getAllDevicesByUserId(userId);
        
        return ResponseEntity.ok(Map.of(
            "totalDevices", allDevices.size(),
            "onlineDevices", onlineCount,
            "offlineDevices", allDevices.size() - onlineCount
        ));
    }

    @PostMapping("/{id}/command")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> sendDeviceCommand(@PathVariable String id, @RequestBody Map<String, Object> command) {
        try {
            // Validate device ownership
            Optional<Device> device = deviceService.getDeviceById(id, currentUserService.getCurrentUserId());
            if (device.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Extract command details
            String commandType = (String) command.get("command");
            Object commandData = command.get("data");
            
            // Send command via MQTT
            String payload = objectMapper.writeValueAsString(Map.of(
                "command", commandType,
                "data", commandData,
                "timestamp", System.currentTimeMillis()
            ));
            
            mqttClientService.publishToDevice(id, "command", payload);
            
            return ResponseEntity.ok(Map.of("message", "Command sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to send command: " + e.getMessage()));
        }
    }
}
