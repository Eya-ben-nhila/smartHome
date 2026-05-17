# Home Assistant Web Integration

## 🌐 Overview
Web dashboard that connects to your Home Assistant instance for real-time device control and monitoring.

## 📁 Files Created
- `homeassistant-dashboard.html` - Main dashboard
- `homeassistant-config.html` - Setup interface  
- `homeassistant-web-integration.js` - API client

## 🚀 Quick Setup

### 1. Configure
1. Open `homeassistant-config.html`
2. Enter your HA URL (e.g., `http://homeassistant.local:8123`)
3. Generate access token in HA: Profile → Long-Lived Access Tokens
4. Test connection and save

### 2. Access Dashboard
1. Open `homeassistant-dashboard.html`
2. Real-time device control and monitoring
3. Energy usage tracking
4. Security event timeline

## ✨ Features
- 📱 Real-time device control
- ⚡ Energy monitoring  
- 🔒 Security events
- 🔄 Auto-discovery
- 📱 Responsive design
- 🔐 Secure authentication

## 🔧 Technical Details
- RESTful API integration
- Bearer token authentication
- Local storage for settings
- Real-time polling (5s intervals)
- Error handling with fallbacks

Open `homeassistant-dashboard.html` to get started!
