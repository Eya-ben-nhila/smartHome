package com.smarthome.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "automations")
public class Automation {
    
    @Id
    private String id;

    @NotBlank
    @Field("automation_name")
    private String automationName;

    @Field("trigger_type")
    private TriggerType triggerType;

    @Field("trigger_data")
    private String triggerData;

    @NotNull
    private ActionType actionType;

    @Field("action_data")
    private String actionData;

    private boolean isActive = true;

    private Long executionCount = 0L;

    @Field("last_executed")
    private LocalDateTime lastExecuted;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    private String userId; // Reference to User ID

    // Constructors
    public Automation() {}

    public Automation(String automationName, TriggerType triggerType, ActionType actionType, String userId) {
        this.automationName = automationName;
        this.triggerType = triggerType;
        this.actionType = actionType;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAutomationName() { return automationName; }
    public void setAutomationName(String automationName) { this.automationName = automationName; }

    public TriggerType getTriggerType() { return triggerType; }
    public void setTriggerType(TriggerType triggerType) { this.triggerType = triggerType; }

    public String getTriggerData() { return triggerData; }
    public void setTriggerData(String triggerData) { this.triggerData = triggerData; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public String getActionData() { return actionData; }
    public void setActionData(String actionData) { this.actionData = actionData; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Long getExecutionCount() { return executionCount; }
    public void setExecutionCount(Long executionCount) { this.executionCount = executionCount; }

    public LocalDateTime getLastExecuted() { return lastExecuted; }
    public void setLastExecuted(LocalDateTime lastExecuted) { this.lastExecuted = lastExecuted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
