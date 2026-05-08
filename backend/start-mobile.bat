@echo off
echo Starting Smart Home Backend for Mobile App...
echo.
echo Make sure MongoDB is running before starting the backend.
echo Backend will be available at: http://localhost:8080
echo Mobile API endpoints: http://localhost:8080/mobile/*
echo.
echo Press Ctrl+C to stop the server
echo.

cd /d "%~dp0"

echo Starting backend server...
call mvnw spring-boot:run

pause
