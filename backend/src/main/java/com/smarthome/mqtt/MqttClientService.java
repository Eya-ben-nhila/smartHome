package com.smarthome.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.entity.Device;
import com.smarthome.entity.DeviceStatus;
import com.smarthome.service.DeviceService;
import com.smarthome.websocket.WebSocketService;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class MqttClientService implements MqttCallback {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private WebSocketService webSocketService;

    private MqttClient mqttClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setConnectionTimeout(10);
            connectOptions.setKeepAliveInterval(60);
            
            if (username != null && !username.isEmpty()) {
                connectOptions.setUserName(username);
                connectOptions.setPassword(password.toCharArray());
            }

            mqttClient.setCallback(this);
            mqttClient.connect(connectOptions);
            
            // Subscribe to device topics
            subscribeToTopics();
            
            System.out.println("MQTT Client connected successfully");
        } catch (MqttException e) {
            System.err.println("MQTT Connection failed: " + e.getMessage());
        }
    }

    private void subscribeToTopics() {
        try {
            // Subscribe to device status updates
            mqttClient.subscribe("smarthome/+/status", 1);
            
            // Subscribe to device data
            mqttClient.subscribe("smarthome/+/data", 1);
            
            // Subscribe to device alerts
            mqttClient.subscribe("smarthome/+/alert", 1);
            
            System.out.println("Subscribed to MQTT topics");
        } catch (MqttException e) {
            System.err.println("Failed to subscribe to topics: " + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("MQTT Connection lost: " + cause.getMessage());
        // Implement reconnection logic
        try {
            Thread.sleep(5000);
            init();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        System.out.println("MQTT Message received - Topic: " + topic + ", Message: " + payload);
        
        try {
            // Parse topic structure: smarthome/{deviceId}/{messageType}
            String[] topicParts = topic.split("/");
            if (topicParts.length >= 3) {
                String deviceIdStr = topicParts[1];
                String messageType = topicParts[2];
                
                try {
                    String deviceId = deviceIdStr;
                    handleDeviceMessage(deviceId, messageType, payload);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid device ID in topic: " + deviceIdStr);
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing MQTT message: " + e.getMessage());
        }
    }

    private void handleDeviceMessage(String deviceId, String messageType, String payload) {
        try {
            switch (messageType) {
                case "status":
                    handleStatusUpdate(deviceId, payload);
                    break;
                case "data":
                    handleDeviceData(deviceId, payload);
                    break;
                case "alert":
                    handleDeviceAlert(deviceId, payload);
                    break;
                default:
                    System.out.println("Unknown message type: " + messageType);
            }
        } catch (Exception e) {
            System.err.println("Error handling device message: " + e.getMessage());
        }
    }

    private void handleStatusUpdate(String deviceId, String payload) {
        try {
            DeviceStatus status = DeviceStatus.valueOf(payload.toUpperCase());
            Device updatedDevice = deviceService.updateDeviceStatusFromSystem(deviceId, status);
            
            // Send real-time update to frontend
            webSocketService.sendDeviceStatusUpdate(updatedDevice);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid device status: " + payload);
        }
    }

    private void handleDeviceData(String deviceId, String payload) {
        // Handle sensor data, device readings, etc.
        System.out.println("Device data received from device " + deviceId + ": " + payload);
        
        // Send real-time data to frontend
        webSocketService.sendDeviceData(deviceId, payload);
    }

    private void handleDeviceAlert(String deviceId, String payload) {
        System.out.println("Device alert from device " + deviceId + ": " + payload);
        
        // Send real-time alert to frontend
        webSocketService.sendDeviceAlert(deviceId, payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Message delivery completed
    }

    public void publishToDevice(String deviceId, String messageType, String payload) {
        try {
            String topic = "smarthome/" + deviceId + "/" + messageType;
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            message.setRetained(false);
            
            mqttClient.publish(topic, message);
            System.out.println("Published to topic " + topic + ": " + payload);
        } catch (MqttException e) {
            System.err.println("Failed to publish message: " + e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
                System.out.println("MQTT Client disconnected");
            }
        } catch (MqttException e) {
            System.err.println("Error disconnecting MQTT client: " + e.getMessage());
        }
    }
}
