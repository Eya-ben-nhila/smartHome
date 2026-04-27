package com.smarthome.automation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.entity.ActionType;
import com.smarthome.entity.Automation;
import com.smarthome.entity.Device;
import com.smarthome.entity.TriggerType;
import com.smarthome.mqtt.MqttClientService;
import com.smarthome.repository.AutomationRepository;
import com.smarthome.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class AutomationEngine {

    @Autowired
    private AutomationRepository automationRepository;

    @Autowired(required = false)
    private MqttClientService mqttClientService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private ObjectMapper objectMapper;

    // Process time-based automations every minute
    @Scheduled(fixedRate = 60000) // Every minute
    public void processTimeBasedAutomations() {
        List<Automation> activeAutomations = automationRepository.findByIsActive(true);
        LocalTime currentTime = LocalTime.now();

        for (Automation automation : activeAutomations) {
            if (automation.getTriggerType() == TriggerType.TIME_BASED) {
                processTimeBasedTrigger(automation, currentTime);
            }
        }
    }

    public void processDeviceTrigger(Long deviceId, TriggerType triggerType, Object triggerData) {
        List<Automation> relevantAutomations = automationRepository
                .findByTriggerTypeAndIsActive(triggerType, true);

        for (Automation automation : relevantAutomations) {
            if (shouldExecuteAutomation(automation, deviceId.toString(), triggerData)) {
                executeAutomation(automation);
            }
        }
    }

    private void processTimeBasedTrigger(Automation automation, LocalTime currentTime) {
        try {
            Map<String, Object> triggerData = objectMapper.readValue(
                    automation.getTriggerData(), Map.class);

            if (triggerData.containsKey("time")) {
                String triggerTime = (String) triggerData.get("time");
                LocalTime targetTime = LocalTime.parse(triggerTime);

                // Check if it's time to execute and hasn't been executed today
                if (currentTime.getHour() == targetTime.getHour() &&
                    currentTime.getMinute() == targetTime.getMinute()) {
                    
                    LocalDateTime lastExecuted = automation.getLastExecuted();
                    if (lastExecuted == null || 
                        lastExecuted.toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {
                        executeAutomation(automation);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing time-based trigger: " + e.getMessage());
        }
    }

    private boolean shouldExecuteAutomation(Automation automation, String deviceId, Object triggerData) {
        try {
            Map<String, Object> triggerConfig = objectMapper.readValue(
                    automation.getTriggerData(), Map.class);

            // For now, assume device matches (in a real app, you'd check if device is part of automation)
            boolean deviceMatches = true;

            // Additional trigger condition checks based on trigger type
            switch (automation.getTriggerType()) {
                case SENSOR_THRESHOLD:
                    return checkSensorThreshold(triggerConfig, triggerData);
                case DEVICE_STATUS_CHANGE:
                    return checkDeviceStatusChange(triggerConfig, triggerData);
                case MOTION_DETECTED:
                    return checkMotionDetected(triggerConfig, triggerData);
                case TEMPERATURE_ABOVE:
                case TEMPERATURE_BELOW:
                    return checkTemperatureThreshold(triggerConfig, triggerData);
                default:
                    return deviceMatches; // For other trigger types, check device matches
            }
        } catch (Exception e) {
            System.err.println("Error checking automation conditions: " + e.getMessage());
            return false;
        }
    }

    private boolean checkSensorThreshold(Map<String, Object> triggerConfig, Object triggerData) {
        try {
            Double threshold = ((Number) triggerConfig.get("threshold")).doubleValue();
            Double currentValue = ((Number) triggerData).doubleValue();
            String operator = (String) triggerConfig.getOrDefault("operator", ">");

            switch (operator) {
                case ">":
                    return currentValue > threshold;
                case "<":
                    return currentValue < threshold;
                case ">=":
                    return currentValue >= threshold;
                case "<=":
                    return currentValue <= threshold;
                case "==":
                    return currentValue.equals(threshold);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkDeviceStatusChange(Map<String, Object> triggerConfig, Object triggerData) {
        String expectedStatus = (String) triggerConfig.get("status");
        return expectedStatus.equals(triggerData.toString());
    }

    private boolean checkMotionDetected(Map<String, Object> triggerConfig, Object triggerData) {
        return Boolean.TRUE.equals(triggerData);
    }

    private boolean checkTemperatureThreshold(Map<String, Object> triggerConfig, Object triggerData) {
        return checkSensorThreshold(triggerConfig, triggerData);
    }

    public void executeAutomation(Automation automation) {
        try {
            System.out.println("Executing automation: " + automation.getAutomationName());

            // Execute action based on action type
            switch (automation.getActionType()) {
                case TURN_ON_DEVICE:
                case TURN_OFF_DEVICE:
                    executeDeviceControl(automation);
                    break;
                case SET_TEMPERATURE:
                    executeTemperatureControl(automation);
                    break;
                case SEND_NOTIFICATION:
                    executeNotification(automation);
                    break;
                case ACTIVATE_ALARM:
                    executeAlarmControl(automation, true);
                    break;
                case DEACTIVATE_ALARM:
                    executeAlarmControl(automation, false);
                    break;
                default:
                    executeGenericAction(automation);
            }

            // Update execution count and last executed time
            automation.setExecutionCount(automation.getExecutionCount() + 1);
            automation.setLastExecuted(LocalDateTime.now());

            // Send WebSocket update
            webSocketService.sendAutomationUpdate(automation);

        } catch (Exception e) {
            System.err.println("Error executing automation: " + e.getMessage());
        }
    }

    private void executeDeviceControl(Automation automation) {
        try {
            Map<String, Object> actionData = objectMapper.readValue(
                    automation.getActionData(), Map.class);

            Long deviceId = ((Number) actionData.get("deviceId")).longValue();
            String command = automation.getActionType() == ActionType.TURN_ON_DEVICE ? 
                    "ON" : "OFF";

            if (mqttClientService != null) {
                mqttClientService.publishToDevice(deviceId.toString(), "control", command);
            } else {
                System.err.println("MQTT service not available - device control skipped");
            }

        } catch (Exception e) {
            System.err.println("Error executing device control: " + e.getMessage());
        }
    }

    private void executeTemperatureControl(Automation automation) {
        try {
            Map<String, Object> actionData = objectMapper.readValue(
                    automation.getActionData(), Map.class);

            Long deviceId = ((Number) actionData.get("deviceId")).longValue();
            Double temperature = ((Number) actionData.get("temperature")).doubleValue();

            String command = "SET_TEMPERATURE:" + temperature;
            if (mqttClientService != null) {
                mqttClientService.publishToDevice(deviceId.toString(), "control", command);
            } else {
                System.err.println("MQTT service not available - temperature control skipped");
            }

        } catch (Exception e) {
            System.err.println("Error executing temperature control: " + e.getMessage());
        }
    }

    private void executeNotification(Automation automation) {
        try {
            Map<String, Object> actionData = objectMapper.readValue(
                    automation.getActionData(), Map.class);

            String message = (String) actionData.get("message");
            String title = (String) actionData.getOrDefault("title", "Automation Alert");

            // Send WebSocket notification
            webSocketService.sendDeviceAlert("0", title + ": " + message);

        } catch (Exception e) {
            System.err.println("Error executing notification: " + e.getMessage());
        }
    }

    private void executeAlarmControl(Automation automation, boolean activate) {
        try {
            Map<String, Object> actionData = objectMapper.readValue(
                    automation.getActionData(), Map.class);

            Long deviceId = ((Number) actionData.get("deviceId")).longValue();
            String command = activate ? "ACTIVATE_ALARM" : "DEACTIVATE_ALARM";

            if (mqttClientService != null) {
                mqttClientService.publishToDevice(deviceId.toString(), "control", command);
            } else {
                System.err.println("MQTT service not available - alarm control skipped");
            }

        } catch (Exception e) {
            System.err.println("Error executing alarm control: " + e.getMessage());
        }
    }

    private void executeGenericAction(Automation automation) {
        try {
            Map<String, Object> actionData = objectMapper.readValue(
                    automation.getActionData(), Map.class);

            Long deviceId = ((Number) actionData.get("deviceId")).longValue();
            String command = automation.getActionType().name();

            if (mqttClientService != null) {
                mqttClientService.publishToDevice(deviceId.toString(), "control", command);
            } else {
                System.err.println("MQTT service not available - generic action skipped");
            }

        } catch (Exception e) {
            System.err.println("Error executing generic action: " + e.getMessage());
        }
    }
}
