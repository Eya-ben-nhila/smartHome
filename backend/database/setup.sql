-- Database setup commands for PostgreSQL
-- Run these commands in pgAdmin or psql shell

-- Create database
CREATE DATABASE smart_home_db;

-- Create user (if not using default postgres user)
CREATE USER smarthome_user WITH PASSWORD 'smarthome123';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE smart_home_db TO smarthome_user;

-- Connect to the database
\c smart_home_db;

-- Verify connection
SELECT current_database();

-- Show tables (should be empty initially)
\dt
