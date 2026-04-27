# Smart Home IoT Backend API Documentation

## Overview

This document provides comprehensive API documentation for the Smart Home IoT Backend system. The API provides RESTful endpoints for managing smart home devices, energy monitoring, automation rules, and real-time communication.

## Base URL

```
http://localhost:8080/api
```

## Authentication

The API uses JWT (JSON Web Token) authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": "64a1b2c3d4e5f6789012345",
  "email": "john@example.com"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "user": {
    "id": "64a1b2c3d4e5f6789012345",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "USER"
  },
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInN1YiI6ImpvaG5AZXhhbXBsZS5jb20iLCJpYXQiOjE2OTg5NzYwMDJ9..."
}
```

#### Health Check
```http
GET /api/auth/health
```

**Response:**
```json
{
  "status": "Auth service is running"
}
```

## Device Management

### Get All Devices
```http
GET /api/devices
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": "64a1b2c3d4e5f6789012345",
    "deviceName": "Smart Light",
    "deviceType": "LIGHT",
    "deviceStatus": "ONLINE",
    "location": "Living Room",
    "manufacturer": "Philips",
    "model": "Hue",
    "firmwareVersion": "1.2.3",
    "isOnline": true,
    "lastSeen": "2026-04-21T14:30:00",
    "createdAt": "2026-04-21T10:00:00"
  }
]
```

### Get Device by ID
```http
GET /api/devices/{deviceId}
Authorization: Bearer <token>
```

### Create Device
```http
POST /api/devices
Authorization: Bearer <token>
Content-Type: application/json

{
  "deviceName": "Smart Thermostat",
  "deviceType": "THERMOSTAT",
  "location": "Hallway",
  "manufacturer": "Nest",
  "model": "Learning Thermostat",
  "firmwareVersion": "5.0.1",
  "macAddress": "00:11:22:33:44:55",
  "ipAddress": "192.168.1.100"
}
```

**Device Types:**
- `LIGHT`
- `THERMOSTAT`
- `MOTION_SENSOR`
- `DOOR_LOCK`
- `SECURITY_CAMERA`
- `SMART_PLUG`
- `TEMPERATURE_SENSOR`
- `HUMIDITY_SENSOR`
- `SMOKE_DETECTOR`
- `WINDOW_BLIND`
- `FAN`
- `AIR_CONDITIONER`
- `HEATER`
- `SPEAKER`
- `VACUUM_CLEANER`
- `GARAGE_DOOR`
- `WATER_LEAK_SENSOR`
- `TV`
- `OTHER`

### Update Device
```http
PUT /api/devices/{deviceId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "deviceName": "Updated Smart Light",
  "location": "Bedroom"
}
```

### Delete Device
```http
DELETE /api/devices/{deviceId}
Authorization: Bearer <token>
```

### Update Device Status
```http
PUT /api/devices/{deviceId}/status
Authorization: Bearer <token>
Content-Type: application/json

{
  "status": "ONLINE"
}
```

**Device Statuses:**
- `ONLINE`
- `OFFLINE`
- `ACTIVE`
- `ERROR`
- `MAINTENANCE`

### Get Online Devices
```http
GET /api/devices/online
Authorization: Bearer <token>
```

### Get Devices by Type
```http
GET /api/devices/type/{deviceType}
Authorization: Bearer <token>
```

### Send Device Command (MQTT)
```http
POST /api/devices/{deviceId}/command
Authorization: Bearer <token>
Content-Type: application/json

{
  "command": "TURN_ON",
  "data": {
    "brightness": 80,
    "color": "#FF0000"
  }
}
```

**Available Commands:**
- `TURN_ON`
- `TURN_OFF`
- `TOGGLE`
- `SET_BRIGHTNESS`
- `SET_COLOR`
- `SET_TEMPERATURE`
- `ADJUST_VOLUME`
- `LOCK_DOOR`
- `UNLOCK_DOOR`

### Get Device Statistics
```http
GET /api/devices/stats
Authorization: Bearer <token>
```

**Response:**
```json
{
  "totalDevices": 15,
  "onlineDevices": 12,
  "offlineDevices": 3
}
```

## Energy Management

### Get Energy Dashboard
```http
GET /api/energy/dashboard
Authorization: Bearer <token>
```

**Response:**
```json
{
  "totalConsumption": 125.5,
  "totalCost": 15.06,
  "activeDevices": 3,
  "readingsToday": 24,
  "averagePower": 45.2,
  "peakPower": 120.0,
  "userId": "64a1b2c3d4e5f6789012345"
}
```

### Get Energy Readings by Device
```http
GET /api/energy/readings/{deviceId}
Authorization: Bearer <token>
```

### Create Energy Reading
```http
POST /api/energy/readings
Authorization: Bearer <token>
Content-Type: application/json

{
  "deviceId": "64a1b2c3d4e5f6789012345",
  "powerUsage": 150.5,
  "voltage": 230.0,
  "current": 0.65,
  "energyConsumption": 1.2,
  "frequency": 50.0,
  "powerFactor": 0.95
}
```

### Get Energy Consumption by Device
```http
GET /api/energy/consumption/{deviceId}?startTime=2026-04-20T00:00:00&endTime=2026-04-21T23:59:59
Authorization: Bearer <token>
```

### Get User Energy Consumption
```http
GET /api/energy/consumption/user?startTime=2026-04-20T00:00:00&endTime=2026-04-21T23:59:59
Authorization: Bearer <token>
```

### Get Recent Energy Readings
```http
GET /api/energy/readings/user/recent?since=2026-04-21T00:00:00
Authorization: Bearer <token>
```

### Get Energy Statistics
```http
GET /api/energy/stats/user?since=2026-04-21T00:00:00
Authorization: Bearer <token>
```

## Automation Management

### Get All Automations
```http
GET /api/automations
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": "64a1b2c3d4e5f6789012346",
    "automationName": "Turn on lights at sunset",
    "triggerType": "TIME_BASED",
    "triggerData": "18:00",
    "actionType": "TURN_ON_DEVICE",
    "actionData": "64a1b2c3d4e5f6789012345",
    "isActive": true,
    "executionCount": 5,
    "lastExecuted": "2026-04-21T18:00:00",
    "createdAt": "2026-04-20T15:30:00",
    "userId": "64a1b2c3d4e5f6789012345"
  }
]
```

### Create Automation
```http
POST /api/automations
Authorization: Bearer <token>
Content-Type: application/json

{
  "automationName": "Morning Routine",
  "triggerType": "TIME_BASED",
  "triggerData": "07:00",
  "actionType": "TURN_ON_DEVICE",
  "actionData": "64a1b2c3d4e5f6789012345",
  "isActive": true
}
```

**Trigger Types:**
- `TIME_BASED`
- `MOTION_DETECTED`
- `DOOR_OPENED`
- `TEMPERATURE_THRESHOLD`
- `ENERGY_THRESHOLD`
- `MANUAL`

**Action Types:**
- `TURN_ON_DEVICE`
- `TURN_OFF_DEVICE`
- `TOGGLE_DEVICE`
- `SET_TEMPERATURE`
- `SET_BRIGHTNESS`
- `SET_COLOR`
- `LOCK_DOOR`
- `UNLOCK_DOOR`
- `SEND_NOTIFICATION`
- `SEND_EMAIL`
- `ACTIVATE_ALARM`
- `DEACTIVATE_ALARM`
- `TAKE_PHOTO`
- `START_RECORDING`
- `STOP_RECORDING`
- `OPEN_BLINDS`
- `CLOSE_BLINDS`
- `DIM_LIGHTS`
- `ADJUST_VOLUME`
- `PLAY_SOUND`
- `CALL_EMERGENCY`
- `SEND_SMS`
- `ACTIVATE_SIREN`
- `FAN_SPEED`
- `HEATER_TEMPERATURE`
- `AC_MODE`
- `VACUUM_START`
- `VACUUM_DOCK`
- `SPRINKLER_ON`
- `SPRINKLER_OFF`
- `CHANGE_CHANNEL`
- `TRIGGER_SCENE`
- `EXECUTE_MACRO`
- `SET_SCHEDULE`
- `UPDATE_STATUS`
- `LOG_EVENT`

### Update Automation
```http
PUT /api/automations/{automationId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "automationName": "Updated Morning Routine",
  "isActive": false
}
```

### Delete Automation
```http
DELETE /api/automations/{automationId}
Authorization: Bearer <token>
```

### Toggle Automation Status
```http
PUT /api/automations/{automationId}/toggle
Authorization: Bearer <token>
```

### Execute Automation Manually
```http
POST /api/automations/{automationId}/execute
Authorization: Bearer <token>
```

### Get Automations by Trigger Type
```http
GET /api/automations/trigger/{triggerType}
Authorization: Bearer <token>
```

### Get Automation Statistics
```http
GET /api/automations/stats
Authorization: Bearer <token>
```

**Response:**
```json
{
  "totalAutomations": 8,
  "activeAutomations": 6,
  "inactiveAutomations": 2,
  "recentExecutions": 12
}
```

## Real-time Communication

### WebSocket Connection

Connect to WebSocket for real-time updates:

```
ws://localhost:8080/api/ws/smarthome
```

**WebSocket Message Types:**

#### Device Status Update
```json
{
  "type": "DEVICE_STATUS_UPDATE",
  "data": {
    "id": "64a1b2c3d4e5f6789012345",
    "deviceName": "Smart Light",
    "deviceStatus": "ONLINE",
    "isOnline": true
  }
}
```

#### Device Data
```json
{
  "type": "DEVICE_DATA",
  "deviceId": "64a1b2c3d4e5f6789012345",
  "data": "{\"temperature\": 22.5, \"humidity\": 45.2}",
  "timestamp": 1698976000000
}
```

#### Device Alert
```json
{
  "type": "DEVICE_ALERT",
  "deviceId": "64a1b2c3d4e5f6789012345",
  "alert": "Motion detected in Living Room",
  "timestamp": 1698976000000
}
```

#### Automation Update
```json
{
  "type": "AUTOMATION_UPDATE",
  "data": {
    "id": "64a1b2c3d4e5f6789012346",
    "automationName": "Turn on lights at sunset",
    "isActive": true,
    "executionCount": 6
  }
}
```

#### Energy Data
```json
{
  "type": "ENERGY_DATA",
  "data": {
    "deviceId": "64a1b2c3d4e5f6789012345",
    "powerUsage": 150.5,
    "energyConsumption": 1.2
  },
  "timestamp": 1698976000000
}
```

#### Security Alert
```json
{
  "type": "SECURITY_ALERT",
  "data": {
    "alertType": "DOOR_FORCED_OPEN",
    "location": "Front Door",
    "severity": "HIGH"
  },
  "timestamp": 1698976000000
}
```

## MQTT Integration

### MQTT Topics

The system uses MQTT for IoT device communication with the following topic structure:

#### Device Status Updates
```
Topic: smarthome/{deviceId}/status
Payload: "ONLINE" | "OFFLINE" | "ACTIVE" | "ERROR"
```

#### Device Data
```
Topic: smarthome/{deviceId}/data
Payload: {"temperature": 22.5, "humidity": 45.2, "powerUsage": 150.5}
```

#### Device Alerts
```
Topic: smarthome/{deviceId}/alert
Payload: {"type": "MOTION_DETECTED", "location": "Living Room"}
```

#### Device Commands
```
Topic: smarthome/{deviceId}/command
Payload: {"command": "TURN_ON", "data": {"brightness": 80}, "timestamp": 1698976000000}
```

## Error Handling

### Standard Error Response Format
```json
{
  "timestamp": "2026-04-21T14:30:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/devices",
  "fieldErrors": {
    "deviceName": "must not be blank",
    "deviceType": "must not be null"
  }
}
```

### Common HTTP Status Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Rate Limiting

API requests are limited to prevent abuse:
- **Authentication endpoints**: 5 requests per minute
- **Device operations**: 100 requests per minute
- **Energy readings**: 1000 requests per minute
- **WebSocket connections**: 10 concurrent connections per user

## Security Considerations

1. **JWT Tokens**: Use secure token storage and refresh mechanisms
2. **HTTPS**: Use HTTPS in production environments
3. **Input Validation**: All inputs are validated and sanitized
4. **Device Ownership**: Users can only access their own devices
5. **MQTT Security**: Use TLS/SSL for MQTT connections in production

## Testing

### Example Test Sequence

1. **Register a user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"password123"}'
```

2. **Login and get token:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

3. **Create a device:**
```bash
curl -X POST http://localhost:8080/api/devices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{"deviceName":"Test Light","deviceType":"LIGHT","location":"Living Room"}'
```

4. **Get all devices:**
```bash
curl -X GET http://localhost:8080/api/devices \
  -H "Authorization: Bearer <your-token>"
```

## Support

For technical support or questions about the API:
- Check the application logs for detailed error messages
- Verify MongoDB and MQTT broker connections
- Ensure JWT tokens are valid and not expired
- Check network connectivity for WebSocket and MQTT connections

---

*This API documentation is for version 1.0.0 of the Smart Home IoT Backend.*
