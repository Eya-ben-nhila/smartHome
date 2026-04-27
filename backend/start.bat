@echo off
REM Smart Home IoT Backend - Quick Start Script for Windows

echo 🚀 Starting Smart Home IoT Backend Setup...

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

echo ✅ Java check passed

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

echo ✅ Maven check passed

REM Create logs directory
if not exist logs mkdir logs

echo 📦 Building application...
mvn clean compile -q

if %errorlevel% neq 0 (
    echo ❌ Build failed
    pause
    exit /b 1
)

echo ✅ Build successful

echo 🚀 Starting Spring Boot application...
echo 📍 Application will be available at: http://localhost:8080
echo 📍 API Documentation: http://localhost:8080/api/auth/health
echo 📍 WebSocket: ws://localhost:8080/ws/smarthome
echo.
echo Press Ctrl+C to stop the application
echo.

REM Start the application
mvn spring-boot:run
