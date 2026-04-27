package com.smarthome.controller;

import com.smarthome.entity.EnergyReading;
import com.smarthome.service.CurrentUserService;
import com.smarthome.service.DeviceService;
import com.smarthome.service.EnergyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/energy")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnergyController {

    @Autowired
    private EnergyService energyService;
    
    @Autowired
    private CurrentUserService currentUserService;
    
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEnergyDashboard() {
        String userId = currentUserService.getCurrentUserId();
        // Return mock dashboard data for testing
        return ResponseEntity.ok(Map.of(
            "totalConsumption", 125.5,
            "totalCost", 15.06,
            "activeDevices", 3,
            "readingsToday", 24,
            "averagePower", 45.2,
            "peakPower", 120.0,
            "userId", userId
        ));
    }

    @GetMapping("/readings/{deviceId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EnergyReading>> getEnergyReadingsByDeviceId(@PathVariable String deviceId) {
        validateDeviceOwnership(deviceId);
        List<EnergyReading> readings = energyService.getAllReadingsByDeviceId(deviceId);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/readings/{deviceId}/range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EnergyReading>> getEnergyReadingsByDeviceIdAndTimeRange(
            @PathVariable String deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        validateDeviceOwnership(deviceId);
        List<EnergyReading> readings = energyService.getReadingsByDeviceIdAndTimeRange(deviceId, startTime, endTime);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/readings/{deviceId}/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EnergyReading>> getRecentEnergyReadings(
            @PathVariable String deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        validateDeviceOwnership(deviceId);
        List<EnergyReading> readings = energyService.getRecentReadingsByDeviceId(deviceId, since);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/readings/{deviceId}/latest")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EnergyReading> getLatestEnergyReading(@PathVariable String deviceId) {
        validateDeviceOwnership(deviceId);
        Optional<EnergyReading> reading = energyService.getLatestReadingByDeviceId(deviceId);
        return reading.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/readings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EnergyReading> createEnergyReading(@Valid @RequestBody EnergyReading energyReading) {
        validateDeviceOwnership(energyReading.getDeviceId());
        energyReading.setUserId(currentUserService.getCurrentUserId());
        EnergyReading createdReading = energyService.createEnergyReading(energyReading);
        return ResponseEntity.ok(createdReading);
    }

    @GetMapping("/consumption/{deviceId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEnergyConsumption(
            @PathVariable String deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        validateDeviceOwnership(deviceId);
        
        Double totalConsumption = energyService.getTotalEnergyConsumption(deviceId, startTime, endTime);
        Double averagePower = energyService.getAveragePowerUsage(deviceId, startTime, endTime);
        Double peakPower = energyService.getPeakPowerUsage(deviceId, startTime, endTime);
        
        return ResponseEntity.ok(Map.of(
                "deviceId", deviceId,
                "startTime", startTime,
                "endTime", endTime,
                "totalConsumption", totalConsumption,
                "averagePower", averagePower,
                "peakPower", peakPower
        ));
    }

    @GetMapping("/consumption/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEnergyConsumptionByUserId(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        String userId = currentUserService.getCurrentUserId();
        
        Double totalConsumption = energyService.getTotalEnergyConsumptionByUserId(userId, startTime, endTime);
        Double totalCost = energyService.getTotalCostByUserId(userId, startTime, endTime);
        
        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "startTime", startTime,
                "endTime", endTime,
                "totalConsumption", totalConsumption,
                "totalCost", totalCost
        ));
    }

    @GetMapping("/readings/user/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EnergyReading>> getRecentEnergyReadingsByUserId(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        String userId = currentUserService.getCurrentUserId();
        List<EnergyReading> readings = energyService.getRecentReadingsByUserId(userId, since);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/stats/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEnergyStatsByUserId(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        String userId = currentUserService.getCurrentUserId();
        
        Long readingCount = energyService.countReadingsByUserIdSince(userId, since);
        List<EnergyReading> recentReadings = energyService.getRecentReadingsByUserId(userId, since);
        
        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "since", since,
                "readingCount", readingCount,
                "recentReadings", recentReadings.size()
        ));
    }

    @PostMapping("/data/{deviceId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> processEnergyData(
            @PathVariable String deviceId,
            @RequestBody String payload) {
        validateDeviceOwnership(deviceId);
        energyService.processEnergyData(deviceId, payload);
        return ResponseEntity.ok(Map.of("message", "Energy data processed successfully"));
    }

    private void validateDeviceOwnership(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            throw new RuntimeException("Device ID is required");
        }
        String userId = currentUserService.getCurrentUserId();
        deviceService.getDeviceById(deviceId, userId)
                .orElseThrow(() -> new RuntimeException("Device not found or access denied"));
    }
}
