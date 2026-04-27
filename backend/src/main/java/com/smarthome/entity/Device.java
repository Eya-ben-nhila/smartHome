package com.smarthome.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "devices")
public class Device {
    
    @Id
    private String id;

    @NotBlank
    @Field("device_name")
    private String deviceName;

    @NotNull
    @Field("device_type")
    private DeviceType deviceType;

    @NotNull
    @Field("device_status")
    private DeviceStatus deviceStatus = DeviceStatus.OFFLINE;

    private String location;
    private String model;
    private String manufacturer;

    @Field("mac_address")
    private String macAddress;

    @Field("ip_address")
    private String ipAddress;

    @Field("firmware_version")
    private String firmwareVersion;

    @Field("is_online")
    private boolean isOnline = false;

    @Field("last_seen")
    private LocalDateTime lastSeen;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    private String userId; // Reference to User ID
    // private List<DeviceEvent> events; // Removed to avoid circular reference
    // private List<Automation> automations; // Removed to avoid circular reference

    // IoT-specific fields for MongoDB flexibility
    private Object deviceData; // Flexible data storage
    private List<String> tags; // Device tags for categorization
    private String firmwareUrl; // URL for firmware updates
    private Integer batteryLevel; // For battery-powered devices
    private String signalStrength; // WiFi/Zigbee signal strength

    // Constructors
    public Device() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
    }

    public Device(String deviceName, DeviceType deviceType, String userId) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public DeviceType getDeviceType() { return deviceType; }
    public void setDeviceType(DeviceType deviceType) { this.deviceType = deviceType; }

    public DeviceStatus getDeviceStatus() { return deviceStatus; }
    public void setDeviceStatus(DeviceStatus deviceStatus) { this.deviceStatus = deviceStatus; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }

    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Object getDeviceData() { return deviceData; }
    public void setDeviceData(Object deviceData) { this.deviceData = deviceData; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getFirmwareUrl() { return firmwareUrl; }
    public void setFirmwareUrl(String firmwareUrl) { this.firmwareUrl = firmwareUrl; }

    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }

    public String getSignalStrength() { return signalStrength; }
    public void setSignalStrength(String signalStrength) { this.signalStrength = signalStrength; }

    // Helper methods for IoT
    public void updateLastSeen() {
        this.lastSeen = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setOnlineStatus(boolean online) {
        this.isOnline = online;
        this.deviceStatus = online ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE;
        updateLastSeen();
    }
}
