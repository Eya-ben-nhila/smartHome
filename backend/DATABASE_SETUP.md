# 🗄️ Database Setup Instructions

## Step-by-Step Guide for Windows

### 1. Install PostgreSQL
**Option A: Official Installer (Recommended)**
1. Download from: https://www.postgresql.org/download/windows/
2. Run the installer
3. Choose these settings:
   - Password: `postgres` (remember this!)
   - Port: `5432`
   - Install pgAdmin 4 (included)

**Option B: Using Chocolatey**
```cmd
# Install Chocolatey (if not installed)
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install PostgreSQL
choco install postgresql --params '/Password:postgres'
```

### 2. Start PostgreSQL Service
```cmd
# Open Services (services.msc)
# Find "postgresql-x64-15" (version may vary)
# Right-click → Start
# Or use command:
net start postgresql-x64-15
```

### 3. Create Database and User

**Method A: Using pgAdmin (Easiest)**
1. Open pgAdmin 4 (installed with PostgreSQL)
2. Connect to server:
   - Host: localhost
   - Port: 5432
   - Username: postgres
   - Password: (what you set during installation)
3. Right-click on "Databases" → "Create" → "Database"
4. Database name: `smart_home_db`
5. Click "Save"
6. Expand "Login/Group Roles" → right-click → "Create" → "Login/Group Role"
7. Username: `smarthome_user`
8. Password: `smarthome123`
9. Go to "Privileges" tab → grant all privileges on `smart_home_db`

**Method B: Using Command Line**
```cmd
# Open psql shell
psql -U postgres

# Run these commands:
CREATE DATABASE smart_home_db;
CREATE USER smarthome_user WITH PASSWORD 'smarthome123';
GRANT ALL PRIVILEGES ON DATABASE smart_home_db TO smarthome_user;
\q
```

### 4. Run Database Schema
1. Open Command Prompt
2. Navigate to project directory:
```cmd
cd c:\Users\eyabe\OneDrive\Desktop\iot-monitoring-platform\backend
```
3. Run schema:
```cmd
psql -h localhost -U smarthome_user -d smart_home_db -f database\schema.sql
```

### 5. Test Database Connection
1. Run the database test:
```cmd
cd c:\Users\eyabe\OneDrive\Desktop\iot-monitoring-platform\backend
mvn spring-boot:run -Dspring-boot.run.main-class=com.smarthome.config.DatabaseTestApplication
```

### 6. Start the Main Application
Once database test passes, start the main application:
```cmd
cd c:\Users\eyabe\OneDrive\Desktop\iot-monitoring-platform\backend
start.bat
```

## 🔍 Verification Steps

### Check PostgreSQL is Running
```cmd
# Check service status
sc query postgresql-x64-15

# Or check if port is listening
netstat -an | findstr :5432
```

### Test Database Connection
Open browser and visit: http://localhost:8080/api/auth/health

You should see: `{"status":"Auth service is running"}`

## 🚨 Troubleshooting

### Common Issues

**Connection Refused**
- PostgreSQL service not running
- Wrong port (5432 is default)
- Firewall blocking connection

**Authentication Failed**
- Wrong username/password
- User doesn't have database privileges
- Database doesn't exist

**Port Already in Use**
- Another application using port 8080
- Change port in application.properties

### Quick Fix Commands
```cmd
# Restart PostgreSQL
net stop postgresql-x64-15
net start postgresql-x64-15

# Check PostgreSQL logs
type "C:\Program Files\PostgreSQL\15\data\log\*.log"

# Test connection manually
psql -h localhost -U smarthome_user -d smart_home_db -c "SELECT version();"
```

## ✅ Success Indicators

You'll know it's working when:
1. ✅ PostgreSQL service is running
2. ✅ Database `smart_home_db` exists
3. ✅ User `smarthome_user` can connect
4. ✅ Schema tables are created
5. ✅ Application starts without database errors
6. ✅ Health endpoint returns success

**Once these are all ✅, your database is ready!** 🎯
