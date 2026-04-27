-- Smart Home IoT Database Schema
-- PostgreSQL Database Setup

-- Create database
-- CREATE DATABASE smart_home_db;

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    profile_image_url VARCHAR(500),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN DEFAULT true,
    is_email_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Devices table
CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    device_name VARCHAR(100) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    device_status VARCHAR(20) DEFAULT 'OFFLINE',
    location VARCHAR(100),
    model VARCHAR(100),
    manufacturer VARCHAR(100),
    mac_address VARCHAR(17) UNIQUE,
    ip_address VARCHAR(45),
    firmware_version VARCHAR(50),
    is_online BOOLEAN DEFAULT false,
    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Automations table
CREATE TABLE automations (
    id BIGSERIAL PRIMARY KEY,
    automation_name VARCHAR(100) NOT NULL,
    trigger_type VARCHAR(50) NOT NULL,
    trigger_data TEXT,
    action_type VARCHAR(50) NOT NULL,
    action_data TEXT,
    is_active BOOLEAN DEFAULT true,
    execution_count BIGINT DEFAULT 0,
    last_executed TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Device Events table
CREATE TABLE device_events (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    event_value DOUBLE PRECISION,
    event_unit VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    device_id BIGINT NOT NULL REFERENCES devices(id) ON DELETE CASCADE
);

-- Energy Readings table
CREATE TABLE energy_readings (
    id BIGSERIAL PRIMARY KEY,
    energy_consumption DOUBLE PRECISION,
    power_usage DOUBLE PRECISION,
    voltage DOUBLE PRECISION,
    current DOUBLE PRECISION,
    frequency DOUBLE PRECISION,
    power_factor DOUBLE PRECISION,
    cost DOUBLE PRECISION,
    reading_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    device_id BIGINT NOT NULL REFERENCES devices(id) ON DELETE CASCADE
);

-- Automation-Device relationship (Many-to-Many)
CREATE TABLE automation_devices (
    automation_id BIGINT NOT NULL REFERENCES automations(id) ON DELETE CASCADE,
    device_id BIGINT NOT NULL REFERENCES devices(id) ON DELETE CASCADE,
    PRIMARY KEY (automation_id, device_id)
);

-- Indexes for performance
CREATE INDEX idx_devices_user_id ON devices(user_id);
CREATE INDEX idx_devices_mac_address ON devices(mac_address);
CREATE INDEX idx_devices_device_type ON devices(device_type);
CREATE INDEX idx_automations_user_id ON automations(user_id);
CREATE INDEX idx_automations_trigger_type ON automations(trigger_type);
CREATE INDEX idx_device_events_device_id ON device_events(device_id);
CREATE INDEX idx_device_events_created_at ON device_events(created_at);
CREATE INDEX idx_energy_readings_device_id ON energy_readings(device_id);
CREATE INDEX idx_energy_readings_reading_time ON energy_readings(reading_time);
CREATE INDEX idx_energy_readings_device_time ON energy_readings(device_id, reading_time);

-- Insert sample data (optional)
INSERT INTO users (full_name, email, password, role) VALUES 
('Admin User', 'admin@smarthome.com', '$2a$10$YourHashedPasswordHere', 'ADMIN'),
('John Doe', 'john@smarthome.com', '$2a$10$YourHashedPasswordHere', 'USER');

INSERT INTO devices (device_name, device_type, device_status, location, mac_address, user_id) VALUES
('Living Room Light', 'LIGHT', 'ONLINE', 'Living Room', 'AA:BB:CC:DD:EE:01', 2),
('Smart Thermostat', 'THERMOSTAT', 'ONLINE', 'Hallway', 'AA:BB:CC:DD:EE:02', 2),
('Security Camera', 'SECURITY_CAMERA', 'ONLINE', 'Front Door', 'AA:BB:CC:DD:EE:03', 2);
