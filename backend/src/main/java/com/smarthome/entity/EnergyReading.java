package com.smarthome.entity;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "energy_readings")
public class EnergyReading {
    @Id
    private String id;

    @NotBlank
    @Field("device_id")
    private String deviceId;

    @Field("user_id")
    private String userId;

    @Field("energy_consumption")
    private Double energyConsumption; // in kWh

    @Field("power_usage")
    private Double powerUsage; // in Watts

    private Double voltage; // in Volts
    private Double current; // in Amps
    private Double frequency; // in Hz

    @Field("power_factor")
    private Double powerFactor;

    private Double cost; // calculated cost

    @Field("reading_time")
    private LocalDateTime readingTime;

    @Field("created_at")
    private LocalDateTime createdAt;

    // Constructors
    public EnergyReading() {}

    public EnergyReading(String deviceId, Double energyConsumption) {
        this.deviceId = deviceId;
        this.energyConsumption = energyConsumption;
        this.readingTime = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Double getEnergyConsumption() { return energyConsumption; }
    public void setEnergyConsumption(Double energyConsumption) { this.energyConsumption = energyConsumption; }

    public Double getPowerUsage() { return powerUsage; }
    public void setPowerUsage(Double powerUsage) { this.powerUsage = powerUsage; }

    public Double getVoltage() { return voltage; }
    public void setVoltage(Double voltage) { this.voltage = voltage; }

    public Double getCurrent() { return current; }
    public void setCurrent(Double current) { this.current = current; }

    public Double getFrequency() { return frequency; }
    public void setFrequency(Double frequency) { this.frequency = frequency; }

    public Double getPowerFactor() { return powerFactor; }
    public void setPowerFactor(Double powerFactor) { this.powerFactor = powerFactor; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public LocalDateTime getReadingTime() { return readingTime; }
    public void setReadingTime(LocalDateTime readingTime) { this.readingTime = readingTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void initializeTimestampsIfMissing() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (readingTime == null) {
            readingTime = LocalDateTime.now();
        }
    }
}
