# Smart Home IoT Backend Setup Guide

## 🚀 QUICK START GUIDE

### 1. Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 13 or higher
- Mosquitto MQTT Broker (optional for testing)

### 2. Database Setup

#### Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql

# Windows
# Download from https://www.postgresql.org/download/windows/
```

#### Create Database
```bash
# Switch to postgres user
sudo -u postgres psql

# Create database and user
CREATE DATABASE smart_home_db;
CREATE USER smarthome_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE smart_home_db TO smarthome_user;
\q
```

#### Run Schema
```bash
psql -h localhost -U smarthome_user -d smart_home_db -f backend/database/schema.sql
```

### 3. MQTT Broker Setup (Optional)

#### Install Mosquitto
```bash
# Ubuntu/Debian
sudo apt install mosquitto mosquitto-clients

# macOS
brew install mosquitto

# Windows
# Download from https://mosquitto.org/download/
```

#### Configure Mosquitto
```bash
# Edit configuration
sudo nano /etc/mosquitto/mosquitto.conf

# Add these lines
listener 1883
allow_anonymous true
persistence true
persistence_location /var/lib/mosquitto/

# Start service
sudo systemctl start mosquitto
sudo systemctl enable mosquitto
```

### 4. Application Configuration

#### Update application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/smart_home_db
spring.datasource.username=smarthome_user
spring.datasource.password=your_password

# MQTT Configuration (if using Mosquitto)
mqtt.broker.url=tcp://localhost:1883
mqtt.client.id=smart-home-server
mqtt.username=
mqtt.password=

# JWT Configuration
jwt.secret=your-super-secret-jwt-key-change-in-production
jwt.expiration=86400000
```

### 5. Build and Run

#### Using Maven
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

#### Using JAR
```bash
mvn clean package
java -jar target/smart-home-backend-0.0.1-SNAPSHOT.jar
```

### 6. Test the Application

#### Health Check
```bash
curl http://localhost:8080/api/auth/health
```

#### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 7. API Endpoints

#### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `GET /api/auth/health` - Health check

#### Devices
- `GET /api/devices` - Get all devices
- `POST /api/devices` - Create device
- `GET /api/devices/{id}` - Get device by ID
- `PUT /api/devices/{id}` - Update device
- `DELETE /api/devices/{id}` - Delete device

#### Automations
- `GET /api/automations` - Get all automations
- `POST /api/automations` - Create automation
- `PUT /api/automations/{id}` - Update automation
- `POST /api/automations/{id}/execute` - Execute automation

#### Energy
- `GET /api/energy/dashboard` - Energy dashboard
- `GET /api/energy/device/{deviceId}` - Device energy data
- `GET /api/energy/user/total` - User energy stats

### 8. WebSocket Connection

#### Connect to WebSocket
```javascript
const ws = new WebSocket('ws://localhost:8080/ws/smarthome');

ws.onmessage = function(event) {
    const data = JSON.parse(event.data);
    console.log('Received:', data);
};
```

### 9. MQTT Topics for IoT Devices

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

### 10. Production Deployment

#### Environment Variables
```bash
export DATABASE_URL=jdbc:postgresql://your-db-host:5432/smart_home_db
export DATABASE_USERNAME=your_db_user
export DATABASE_PASSWORD=your_db_password
export MQTT_BROKER_URL=tcp://your-mqtt-broker:1883
export JWT_SECRET=your-production-secret
```

#### Docker Deployment (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/smart-home-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### Build Docker Image
```bash
docker build -t smart-home-backend .
docker run -p 8080:8080 smart-home-backend
```

## 🔧 TROUBLESHOOTING

### Common Issues

#### Database Connection
- Check PostgreSQL is running: `sudo systemctl status postgresql`
- Verify connection string in application.properties
- Check firewall settings

#### MQTT Connection
- Verify Mosquitto is running: `sudo systemctl status mosquitto`
- Check port 1883 is accessible
- Test with: `mosquitto_pub -h localhost -t test -m "hello"`

#### JWT Issues
- Verify secret key is set
- Check token expiration time
- Validate token format

### 📊 Monitoring

#### Application Logs
```bash
# View logs
tail -f logs/application.log

# Check database queries
# Set logging.level.org.springframework.web=DEBUG in application.properties
```

#### Database Performance
```sql
-- Check slow queries
SELECT query, mean_time, calls 
FROM pg_stat_statements 
ORDER BY mean_time DESC 
LIMIT 10;
```

## 🎯 NEXT STEPS

1. **Connect Frontend**: Update Angular app to use backend APIs
2. **Device Simulators**: Create mock IoT devices for testing
3. **Real Testing**: Connect actual IoT hardware
4. **Security Hardening**: Implement rate limiting, input validation
5. **Monitoring**: Add application monitoring and alerting
6. **Scaling**: Implement load balancing and caching

**Your Smart Home IoT backend is ready for production!** 🚀
