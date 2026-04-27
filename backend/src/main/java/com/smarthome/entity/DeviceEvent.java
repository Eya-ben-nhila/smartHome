package com.smarthome.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "device_events")
public class DeviceEvent {
    @Id
    private String id;

    @NotNull
    @Field("event_type")
    private EventType eventType;

    @Field("event_data")
    private String eventData;

    @Field("event_value")
    private Double eventValue;

    @Field("event_unit")
    private String eventUnit;

    @Field("created_at")
    private LocalDateTime createdAt;

    @NotBlank
    @Field("device_id")
    private String deviceId;

    @Field("user_id")
    private String userId;

    // Constructors
    public DeviceEvent() {}

    public DeviceEvent(EventType eventType, String deviceId) {
        this.eventType = eventType;
        this.deviceId = deviceId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }

    public Double getEventValue() { return eventValue; }
    public void setEventValue(Double eventValue) { this.eventValue = eventValue; }

    public String getEventUnit() { return eventUnit; }
    public void setEventUnit(String eventUnit) { this.eventUnit = eventUnit; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public void initializeCreatedAtIfMissing() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
