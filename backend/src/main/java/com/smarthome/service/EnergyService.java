package com.smarthome.service;

import com.smarthome.entity.EnergyReading;
import com.smarthome.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnergyService {

    @Autowired
    private EnergyReadingRepository energyReadingRepository;

    public List<EnergyReading> getAllReadingsByDeviceId(String deviceId) {
        return energyReadingRepository.findByDeviceIdOrderByReadingTimeDesc(deviceId);
    }

    public List<EnergyReading> getReadingsByDeviceIdAndTimeRange(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return energyReadingRepository.findByDeviceIdAndReadingTimeBetweenOrderByReadingTimeDesc(deviceId, startTime, endTime);
    }

    public List<EnergyReading> getRecentReadingsByDeviceId(String deviceId, LocalDateTime since) {
        return energyReadingRepository.findRecentReadingsByDeviceId(deviceId, since);
    }

    public EnergyReading createEnergyReading(EnergyReading energyReading) {
        // Calculate cost based on energy consumption (assuming $0.12 per kWh)
        if (energyReading.getEnergyConsumption() != null) {
            double costPerKwh = 0.12;
            energyReading.setCost(energyReading.getEnergyConsumption() * costPerKwh);
        }
        energyReading.initializeTimestampsIfMissing();
        return energyReadingRepository.save(energyReading);
    }

    public Double getTotalEnergyConsumption(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return energyReadingRepository.getTotalEnergyConsumption(deviceId, startTime, endTime);
    }

    public Double getAveragePowerUsage(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return energyReadingRepository.getAveragePowerUsage(deviceId, startTime, endTime);
    }

    public Double getPeakPowerUsage(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return energyReadingRepository.getPeakPowerUsage(deviceId, startTime, endTime);
    }

    public Double getTotalEnergyConsumptionByUserId(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return energyReadingRepository.getTotalEnergyConsumptionByUserId(userId, startTime, endTime);
    }

    public Double getTotalCostByUserId(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return energyReadingRepository.getTotalCostByUserId(userId, startTime, endTime);
    }

    public List<EnergyReading> getRecentReadingsByUserId(String userId, LocalDateTime since) {
        return energyReadingRepository.findRecentReadingsByUserId(userId, since);
    }

    public Long countReadingsByUserIdSince(String userId, LocalDateTime since) {
        return energyReadingRepository.countReadingsByUserIdSince(userId, since);
    }

    public Optional<EnergyReading> getLatestReadingByDeviceId(String deviceId) {
        List<EnergyReading> readings = energyReadingRepository.findByDeviceIdOrderByReadingTimeDesc(deviceId);
        return readings.isEmpty() ? Optional.empty() : Optional.of(readings.get(0));
    }

    public void processEnergyData(String deviceId, String payload) {
        try {
            // Parse energy data from MQTT payload
            // Expected format: {"powerUsage": 150.5, "voltage": 230.0, "current": 0.65}
            EnergyReading reading = new EnergyReading();
            reading.setDeviceId(deviceId);
            
            // Set device (would be fetched from repository)
            // reading.setDevice(deviceRepository.findById(deviceId).orElse(null));
            
            // Parse JSON payload and set values
            // This is a simplified version - in production, use proper JSON parsing
            if (payload.contains("powerUsage")) {
                String powerStr = payload.substring(payload.indexOf("\"powerUsage\":") + 12);
                powerStr = powerStr.substring(0, powerStr.indexOf(","));
                reading.setPowerUsage(Double.parseDouble(powerStr));
            }
            
            if (payload.contains("voltage")) {
                String voltageStr = payload.substring(payload.indexOf("\"voltage\":") + 10);
                voltageStr = voltageStr.substring(0, voltageStr.indexOf(","));
                reading.setVoltage(Double.parseDouble(voltageStr));
            }
            
            if (payload.contains("current")) {
                String currentStr = payload.substring(payload.indexOf("\"current\":") + 10);
                currentStr = currentStr.substring(0, currentStr.indexOf("}"));
                reading.setCurrent(Double.parseDouble(currentStr));
            }
            
            createEnergyReading(reading);
            
        } catch (Exception e) {
            System.err.println("Error processing energy data: " + e.getMessage());
        }
    }
}
