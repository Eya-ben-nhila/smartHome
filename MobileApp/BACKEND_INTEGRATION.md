# Mobile App Backend Integration

This document describes the backend integration setup for the Smart Home mobile app.

## Overview

The mobile app is now integrated with the Spring Boot backend using the same API structure as the website. The integration includes:

- JWT-based authentication
- Device management (CRUD operations)
- Energy monitoring
- Automation rules
- Security alerts
- Real-time communication support (WebSockets, MQTT)

## Architecture

### Data Layer

The backend integration follows a clean architecture pattern:

```
├── data/
│   ├── api/
│   │   ├── model/           # Backend-specific data models
│   │   │   ├── BackendAuthModels.kt
│   │   │   └── BackendDeviceModels.kt
│   │   ├── NetworkClient.kt    # Retrofit configuration
│   │   └── SmartHomeApiService.kt  # API interface definitions
│   └── repository/
│       └── SmartHomeRepository.kt  # Business logic layer
```

### Key Components

1. **NetworkClient.kt**
   - Configures Retrofit with OkHttp
   - Base URL: `http://10.0.2.2:8080/` (Android emulator localhost)
   - Includes logging interceptor for debugging
   - 30-second timeout for network operations

2. **SmartHomeApiService.kt**
   - Defines all REST API endpoints
   - Uses JWT Bearer token authentication
   - Covers: Auth, Devices, Energy, Automation, Alerts

3. **SmartHomeRepository.kt**
   - Handles API calls with coroutines
   - Returns `Result<T>` for success/failure handling
   - Manages error handling and network operations

4. **Backend Models**
   - `BackendAuthResponse`, `BackendUser`, `BackendLoginRequest`, `BackendRegisterRequest`
   - `BackendDevice`, `BackendDeviceRequest`, `BackendDeviceStatusRequest`, `BackendDeviceCommandRequest`
   - `BackendDeviceStats`

## Authentication Flow

1. User enters credentials in SigninActivity
2. App calls `repository.login(email, password)`
3. Backend validates and returns JWT token + user data
4. App stores:
   - JWT token in `AppPreferences.setJwtToken()`
   - User ID in `AppPreferences.setUserId()`
   - User name in `AppPreferences.setUserName()`
5. Subsequent API calls include `Authorization: Bearer <token>` header

## Configuration

### For Android Emulator
- Base URL: `http://10.0.2.2:8080/`
- No additional configuration needed

### For Real Device
Update `NetworkClient.kt` to use your computer's IP:
```kotlin
private const val BASE_URL = "http://192.168.1.100:8080/"
```

### AndroidManifest.xml
- `INTERNET` permission: ✅ Already added
- `ACCESS_NETWORK_STATE` permission: ✅ Already added
- `usesCleartextTraffic="true"`: ✅ Added for HTTP support

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/health` - Health check

### Devices
- `GET /api/devices` - Get all devices
- `POST /api/devices` - Create device
- `PUT /api/devices/{id}` - Update device
- `DELETE /api/devices/{id}` - Delete device
- `PUT /api/devices/{id}/status` - Update device status
- `POST /api/devices/{id}/command` - Send command to device
- `GET /api/devices/stats` - Get device statistics

### Energy
- `GET /api/energy/dashboard` - Get energy dashboard data
- `GET /api/energy/readings/{deviceId}` - Get energy readings
- `POST /api/energy/readings` - Create energy reading

### Automation
- `GET /api/automations` - Get all automations
- `POST /api/automations` - Create automation
- `PUT /api/automations/{id}` - Update automation
- `DELETE /api/automations/{id}` - Delete automation
- `PUT /api/automations/{id}/toggle` - Toggle automation
- `POST /api/automations/{id}/execute` - Execute automation

### Alerts
- `GET /api/alerts` - Get all alerts
- `PUT /api/alerts/{id}/resolve` - Resolve alert

## Usage Example

### Login
```kotlin
val repository = SmartHomeRepository()
lifecycleScope.launch {
    val result = repository.login(email, password)
    result.onSuccess { authResponse ->
        // Save token and user data
        AppPreferences.setJwtToken(authResponse.token!!)
        AppPreferences.setUserId(authResponse.userId!!)
        // Navigate to dashboard
    }.onFailure { error ->
        // Handle error
        Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
    }
}
```

### Get Devices
```kotlin
val token = AppPreferences.getJwtToken() ?: return
lifecycleScope.launch {
    val result = repository.getAllDevices(token)
    result.onSuccess { devices ->
        // Display devices
    }.onFailure { error ->
        // Handle error
    }
}
```

## Next Steps

1. **Update DashboardActivity** to load devices from backend instead of local storage
2. **Update EnergyActivity** to fetch energy data from backend
3. **Update AutomationActivity** to manage automations via backend
4. **Update AlertsActivity** to fetch and resolve alerts from backend
5. **Implement MQTT integration** for real-time device updates
6. **Add WebSocket support** for live notifications

## Notes

- The backend uses JWT authentication with Bearer tokens
- All API calls are asynchronous using Kotlin coroutines
- Network errors are wrapped in `Result<T>` for easy handling
- The app maintains local storage via `AppPreferences` for offline support
- Remember Me functionality stores credentials locally for quick login

## Dependencies

- Retrofit 2.9.0 - HTTP client
- Gson 2.10.1 - JSON parsing
- OkHttp 4.12.0 - HTTP client with logging
- Coroutines 1.7.3 - Asynchronous programming

## Backend Documentation

For detailed backend API documentation, see:
- `backend/API_DOCUMENTATION.md` - Complete API reference
- `backend/MOBILE_API_README.md` - Mobile-specific API guide
