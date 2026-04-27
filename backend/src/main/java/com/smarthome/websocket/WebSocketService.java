package com.smarthome.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
        System.out.println("WebSocket session added: " + sessionId);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
        System.out.println("WebSocket session removed: " + sessionId);
    }

    public void sendDeviceStatusUpdate(Object device) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "type", "DEVICE_STATUS_UPDATE",
                "data", device
            ));
            broadcast(message);
        } catch (IOException e) {
            System.err.println("Error sending device status update: " + e.getMessage());
        }
    }

    public void sendDeviceData(String deviceId, String data) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "type", "DEVICE_DATA",
                "deviceId", deviceId,
                "data", data,
                "timestamp", System.currentTimeMillis()
            ));
            broadcast(message);
        } catch (IOException e) {
            System.err.println("Error sending device data: " + e.getMessage());
        }
    }

    public void sendDeviceAlert(String deviceId, String alert) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "type", "DEVICE_ALERT",
                "deviceId", deviceId,
                "alert", alert,
                "timestamp", System.currentTimeMillis()
            ));
            broadcast(message);
        } catch (IOException e) {
            System.err.println("Error sending device alert: " + e.getMessage());
        }
    }

    public void sendAutomationUpdate(Object automation) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "type", "AUTOMATION_UPDATE",
                "data", automation
            ));
            broadcast(message);
        } catch (IOException e) {
            System.err.println("Error sending automation update: " + e.getMessage());
        }
    }

    public void sendEnergyData(Object energyData) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "type", "ENERGY_DATA",
                "data", energyData,
                "timestamp", System.currentTimeMillis()
            ));
            broadcast(message);
        } catch (IOException e) {
            System.err.println("Error sending energy data: " + e.getMessage());
        }
    }

    public void sendSecurityAlert(Object securityAlert) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "type", "SECURITY_ALERT",
                "data", securityAlert,
                "timestamp", System.currentTimeMillis()
            ));
            broadcast(message);
        } catch (IOException e) {
            System.err.println("Error sending security alert: " + e.getMessage());
        }
    }

    private void broadcast(String message) {
        sessions.values().removeIf(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                    return false;
                } else {
                    return true; // Remove closed sessions
                }
            } catch (IOException e) {
                System.err.println("Error sending message to WebSocket session: " + e.getMessage());
                return true; // Remove problematic sessions
            }
        });
    }

    public int getActiveSessionCount() {
        return (int) sessions.values().stream()
                .filter(WebSocketSession::isOpen)
                .count();
    }
}
