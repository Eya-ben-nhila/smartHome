@echo off
echo 🚀 MongoDB Setup for Smart Home IoT
echo.

echo Step 1: Checking MongoDB...
netstat -an | findstr :27017
if %errorlevel% equ 0 (
    echo ✅ MongoDB is running on port 27017
) else (
    echo ❌ MongoDB is not running
    echo Please start MongoDB service first
    pause
    exit /b 1
)

echo.
echo Step 2: Checking Maven...
mvn --version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Maven is installed
) else (
    echo ❌ Maven is not installed
    echo Installing Maven using Chocolatey...
    
    REM Check if Chocolatey is installed
    choco --version >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ Chocolatey found, installing Maven...
        choco install maven -y
    ) else (
        echo ❌ Chocolatey not found
        echo Please install Maven manually from: https://maven.apache.org/download.cgi
        echo Or install Chocolatey first: https://chocolatey.org/install
        pause
        exit /b 1
    )
)

echo.
echo Step 3: Testing Spring Boot connection...
echo Starting application...
echo.

cd backend
mvn spring-boot:run

pause
