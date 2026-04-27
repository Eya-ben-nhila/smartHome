#!/bin/bash

# Smart Home IoT Backend - Quick Start Script

echo "🚀 Starting Smart Home IoT Backend Setup..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version check passed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

echo "✅ Maven check passed"

# Check if PostgreSQL is running
if ! pg_isready -q; then
    echo "⚠️  PostgreSQL is not running. Please start PostgreSQL service."
    echo "   Ubuntu/Debian: sudo systemctl start postgresql"
    echo "   macOS: brew services start postgresql"
    echo "   Windows: Start PostgreSQL service from Services"
fi

# Create logs directory
mkdir -p logs

echo "📦 Building application..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Build successful"
else
    echo "❌ Build failed"
    exit 1
fi

echo "🚀 Starting Spring Boot application..."
echo "📍 Application will be available at: http://localhost:8080"
echo "📍 API Documentation: http://localhost:8080/api/auth/health"
echo "📍 WebSocket: ws://localhost:8080/ws/smarthome"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

# Start the application
mvn spring-boot:run
