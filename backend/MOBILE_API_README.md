# Smart Home Mobile API

This document describes the bare minimum backend API for the mobile app.

## Quick Start

### 1. Start the Backend
```bash
# Windows
cd backend
start-mobile.bat

# Or manually:
mvnw spring-boot:run
```

### 2. Backend URL
- **Local**: `http://localhost:8080`
- **Mobile API Base**: `http://localhost:8080/mobile`

### 3. Test Connection
```bash
curl http://localhost:8080/mobile/health
```

## Mobile API Endpoints

### Authentication
- `POST /mobile/login` - Simple login (no real auth for demo)
- `GET /mobile/health` - Health check

### Devices
- `GET /mobile/devices` - Get all devices
- `GET /mobile/devices/{id}` - Get specific device
- `PUT /mobile/devices/{id}/status` - Update device status
- `POST /mobile/devices` - Add new device
- `DELETE /mobile/devices/{id}` - Delete device

### Data
- `GET /mobile/energy` - Energy consumption data
- `GET /mobile/security` - Security events
- `GET /mobile/automations` - Automations list
- `GET /mobile/profile` - User profile
- `GET /mobile/alerts` - Alerts/notifications

## Mobile App Configuration

### Android Emulator
The mobile app is configured to use `http://10.0.2.2:8080` (Android emulator localhost)

### Real Device
Change `BASE_URL` in `ApiService.kt` to your computer's IP:
```kotlin
private const val BASE_URL = "http://192.168.1.100:8080"
```

## Sample Responses

### Login Response
```json
{
  "message": "Login successful",
  "token": "mobile_token_1234567890",
  "userId": "demo_user_123456",
  "email": "user@example.com"
}
```

### Devices Response
```json
{
  "success": true,
  "devices": [
    {
      "id": "1",
      "name": "Living Room Light",
      "type": "LIGHT",
      "status": "ON",
      "value": 75,
      "room": "Living Room",
      "icon": "lightbulb"
    }
  ],
  "total": 1
}
```

## Features Implemented

### ✅ Bare Minimum Functionality
- **Authentication** - Simple login with dummy tokens
- **Device Management** - CRUD operations for devices
- **Real-time Data** - Energy, security, automation data
- **Offline Support** - Fallback to local storage
- **Error Handling** - Graceful degradation

### ✅ Mobile-Specific Features
- **Simplified Responses** - Lightweight JSON responses
- **No Authentication Required** - Easy for demo/testing
- **CORS Enabled** - Works with mobile apps
- **Sample Data** - Pre-populated with demo data

## Security Notes

⚠️ **This is a demo setup** - No real authentication is implemented. The mobile endpoints are open for easy testing.

For production:
1. Implement proper JWT authentication
2. Add rate limiting
3. Validate input data
4. Use HTTPS
5. Add proper error handling

## Testing

### Test with curl
```bash
# Health check
curl http://localhost:8080/mobile/health

# Login
curl -X POST http://localhost:8080/mobile/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password"}'

# Get devices
curl http://localhost:8080/mobile/devices
```

### Test in Browser
Open these URLs in your browser:
- http://localhost:8080/mobile/health
- http://localhost:8080/mobile/devices
- http://localhost:8080/mobile/energy

## Troubleshooting

### Backend Not Starting
- Make sure MongoDB is running
- Check if port 8080 is available
- Run `mvnw clean install` first

### Mobile App Can't Connect
- Check backend is running: `curl http://localhost:8080/mobile/health`
- For real device, use your computer's IP address
- Make sure firewall allows port 8080

### CORS Issues
The backend is configured to allow all origins for demo purposes.

## Next Steps

To make this production-ready:
1. Add real user authentication
2. Connect to actual IoT devices
3. Implement real-time updates with WebSocket
4. Add proper data validation
5. Implement database persistence
6. Add comprehensive error handling
7. Add logging and monitoring
