# 🗄️ MongoDB Setup for Smart Home IoT

## 🚀 Why MongoDB for IoT?

**Perfect for Smart Home Data:**
- ✅ **Flexible Schema**: Different devices have different data
- ✅ **Time Series**: Natural for sensor readings
- ✅ **High Performance**: Fast writes for real-time data
- ✅ **Scalability**: Easy to add more devices
- ✅ **Document Storage**: JSON fits IoT data perfectly

## 📦 Installation Options

### Option A: MongoDB Atlas (Cloud - Recommended for Development)
1. Go to https://www.mongodb.com/cloud/atlas
2. Create free account
3. Create free cluster (512MB)
4. Get connection string
5. Update application.properties with your connection string

### Option B: Local MongoDB Installation

#### Windows
```cmd
# Download from: https://www.mongodb.com/try/download/community
# Run installer with default settings
# MongoDB will run on port 27017
```

#### Using Chocolatey
```cmd
choco install mongodb
```

#### Using Docker
```cmd
docker run --name mongodb -p 27017:27017 -d mongo:latest
```

## 🔧 Configuration

### Update application.properties
```properties
# For local MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/smart_home_db

# For MongoDB Atlas
# spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/smart_home_db

# For Docker
# spring.data.mongodb.uri=mongodb://host.docker.internal:27017/smart_home_db
```

## 📊 MongoDB Collections for IoT

### Users Collection
```json
{
  "_id": "user123",
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "isActive": true,
  "createdAt": "2024-01-01T10:00:00Z",
  "updatedAt": "2024-01-01T10:00:00Z"
}
```

### Devices Collection (IoT Flexible)
```json
{
  "_id": "device123",
  "deviceName": "Living Room Light",
  "deviceType": "LIGHT",
  "deviceStatus": "ONLINE",
  "location": "Living Room",
  "macAddress": "AA:BB:CC:DD:EE:01",
  "isOnline": true,
  "userId": "user123",
  "deviceData": {
    "brightness": 75,
    "color": "#FFFFFF",
    "powerConsumption": 12.5
  },
  "tags": ["lighting", "main-room"],
  "batteryLevel": null,
  "signalStrength": "excellent",
  "lastSeen": "2024-01-01T10:00:00Z"
}
```

### Device Events Collection (Time Series)
```json
{
  "_id": "event123",
  "eventType": "SENSOR_READING",
  "eventData": "Temperature: 22.5°C, Humidity: 45%",
  "eventValue": 22.5,
  "eventUnit": "celsius",
  "deviceId": "device123",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

### Energy Readings Collection (Time Series)
```json
{
  "_id": "energy123",
  "energyConsumption": 0.125,
  "powerUsage": 150.5,
  "voltage": 230.0,
  "current": 0.65,
  "cost": 0.015,
  "deviceId": "device123",
  "readingTime": "2024-01-01T10:00:00Z"
}
```

## 🚀 Quick Start Commands

### Test MongoDB Connection
```bash
# Connect to MongoDB
mongosh

# List databases
show dbs

# Switch to smart home database
use smart_home_db

# Show collections
show collections

# Insert test user
db.users.insertOne({
  fullName: "Test User",
  email: "test@smarthome.com",
  password: "$2a$10$...",
  role: "USER",
  isActive: true,
  createdAt: new Date(),
  updatedAt: new Date()
})

# Find users
db.users.find().pretty()
```

### Start Spring Boot Application
```bash
cd backend
mvn spring-boot:run
```

### Test API
```bash
curl http://localhost:8080/api/auth/health
```

## 🔍 MongoDB Advantages for IoT

### 1. Flexible Schema
```json
// Different devices can have different data structures
// Smart Light
{
  "deviceType": "LIGHT",
  "deviceData": {
    "brightness": 75,
    "color": "#FFFFFF",
    "powerConsumption": 12.5
  }
}

// Temperature Sensor
{
  "deviceType": "TEMPERATURE_SENSOR",
  "deviceData": {
    "temperature": 22.5,
    "humidity": 45,
    "calibrationOffset": 0.2
  }
}
```

### 2. Time Series Data
```javascript
// Easy to store and query time-based sensor data
db.energyReadings.find({
  "deviceId": "device123",
  "readingTime": {
    $gte: ISODate("2024-01-01T00:00:00Z"),
    $lt: ISODate("2024-01-02T00:00:00Z")
  }}).sort({readingTime: -1})
```

### 3. Aggregation Pipeline
```javascript
// Calculate energy consumption by device type
db.energyReadings.aggregate([
  {
    $group: {
      _id: "$deviceType",
      totalConsumption: { $sum: "$energyConsumption" },
      averagePower: { $avg: "$powerUsage" }
    }
  }
])
```

### 4. Geospatial Queries
```javascript
// Find devices by location (if GPS enabled)
db.devices.find({
  "location": {
    $near: {
      $geometry: { type: "Point", coordinates: [longitude, latitude] },
      $maxDistance: 1000 // meters
    }
  }
})
```

## 🎯 IoT-Specific Benefits

### High-Frequency Data
- **Fast Writes**: Optimized for millions of sensor readings
- **Bulk Inserts**: Efficient batch operations
- **Compression**: Reduced storage for time series data

### Real-Time Analytics
- **Change Streams**: Real-time data notifications
- **Aggregation**: Built-in analytics capabilities
- **Indexing**: Optimized for time-based queries

### Scalability
- **Sharding**: Distribute data across servers
- **Replication**: High availability
- **Auto-scaling**: Handle growing device counts

## 🚨 Troubleshooting

### Connection Issues
```bash
# Check MongoDB status
# Windows: Check Services for MongoDB
# Docker: docker ps | grep mongo

# Test connection
mongosh --host localhost --port 27017
```

### Common Errors
- **Connection refused**: MongoDB not running
- **Authentication failed**: Wrong credentials
- **Database not found**: Database not created yet

### Performance Tips
- Create indexes on frequently queried fields
- Use aggregation for complex analytics
- Consider time series collections for sensor data

## ✅ Success Indicators

You'll know it's working when:
1. ✅ MongoDB service is running
2. ✅ Application connects to database
3. ✅ Collections are created automatically
4. ✅ API endpoints return data
5. ✅ Device data is stored correctly

**MongoDB is perfect for your IoT Smart Home application!** 🎯
