// Home Assistant Web Integration
// This script connects to your Home Assistant instance and displays real-time data

class HomeAssistantWebIntegration {
    constructor() {
        this.haUrl = 'http://homeassistant.local:8123'; // Change to your HA URL
        this.accessToken = 'your_homeassistant_token_here'; // Change to your HA token
        this.isConnected = false;
        this.devices = [];
        this.energyData = null;
        this.securityEvents = [];
        
        this.init();
    }

    async init() {
        console.log('🏠 Initializing Home Assistant Web Integration...');
        
        // Test connection on load
        await this.testConnection();
        
        if (this.isConnected) {
            console.log('✅ Connected to Home Assistant successfully!');
            // Start real-time updates
            this.startRealTimeUpdates();
            // Load initial data
            await this.loadAllData();
        } else {
            console.log('❌ Failed to connect to Home Assistant');
            this.showConnectionError();
        }
    }

    async testConnection() {
        try {
            console.log(`🔍 Testing connection to ${this.haUrl}...`);
            
            const response = await fetch(`${this.haUrl}/api/`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                this.isConnected = true;
                this.updateConnectionStatus('Connected', 'success');
                return true;
            } else {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
        } catch (error) {
            console.error('❌ Connection test failed:', error);
            this.isConnected = false;
            this.updateConnectionStatus('Connection Failed', 'error');
            return false;
        }
    }

    async loadAllData() {
        console.log('📊 Loading all data from Home Assistant...');
        
        // Load devices
        await this.loadDevices();
        
        // Load energy data
        await this.loadEnergyData();
        
        // Load security events
        await this.loadSecurityEvents();
        
        console.log('✅ All data loaded successfully!');
    }

    async loadDevices() {
        try {
            const response = await fetch(`${this.haUrl}/api/states`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const states = await response.json();
                this.devices = this.parseDevices(states);
                this.updateDevicesDisplay();
                console.log(`📱 Loaded ${this.devices.length} devices`);
            } else {
                console.error('❌ Failed to load devices');
            }
        } catch (error) {
            console.error('❌ Error loading devices:', error);
        }
    }

    async loadEnergyData() {
        try {
            const response = await fetch(`${this.haUrl}/api/states`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const states = await response.json();
                this.energyData = this.parseEnergyData(states);
                this.updateEnergyDisplay();
                console.log('⚡ Energy data loaded');
            } else {
                console.error('❌ Failed to load energy data');
            }
        } catch (error) {
            console.error('❌ Error loading energy data:', error);
        }
    }

    async loadSecurityEvents() {
        try {
            const response = await fetch(`${this.haUrl}/api/states`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const states = await response.json();
                this.securityEvents = this.parseSecurityEvents(states);
                this.updateSecurityDisplay();
                console.log(`🔒 Loaded ${this.securityEvents.length} security events`);
            } else {
                console.error('❌ Failed to load security events');
            }
        } catch (error) {
            console.error('❌ Error loading security events:', error);
        }
    }

    parseDevices(states) {
        return states
            .filter(entity => this.isDevice(entity.entity_id))
            .map(entity => ({
                id: entity.entity_id,
                name: entity.attributes?.friendly_name || entity.entity_id,
                state: entity.state,
                type: this.getDeviceType(entity.entity_id, entity.attributes?.device_class),
                room: this.getDeviceRoom(entity.entity_id),
                lastChanged: entity.last_changed,
                icon: this.getDeviceIcon(entity.entity_id, entity.attributes?.device_class),
                attributes: {
                    brightness: entity.state === 'on' ? 100 : 0,
                    power: parseFloat(entity.state) || 0,
                    temperature: entity.attributes?.temperature,
                    humidity: entity.attributes?.humidity,
                }
            }));
    }

    parseEnergyData(states) {
        let totalPower = 0;
        let totalEnergy = 0;
        const energyDevices = [];

        states.forEach(entity => {
            if (entity.entity_id.includes('energy') || entity.entity_id.includes('power')) {
                const value = parseFloat(entity.state) || 0;
                if (entity.entity_id.includes('power')) {
                    totalPower += value;
                } else if (entity.entity_id.includes('energy')) {
                    totalEnergy += value;
                }

                energyDevices.push({
                    id: entity.entity_id,
                    name: entity.attributes?.friendly_name || entity.entity_id,
                    value: value,
                    unit: entity.attributes?.unit_of_measurement || 'W',
                    lastUpdated: entity.last_changed
                });
            }
        });

        return {
            totalPowerConsumption: totalPower,
            totalEnergyConsumption: totalEnergy,
            devices: energyDevices,
            timestamp: new Date().toISOString()
        };
    }

    parseSecurityEvents(states) {
        return states
            .filter(entity => this.isSecurityEntity(entity.entity_id))
            .map(entity => ({
                id: entity.entity_id,
                name: entity.attributes?.friendly_name || entity.entity_id,
                state: entity.state,
                eventType: this.getEventType(entity.entity_id, entity.state),
                location: entity.attributes?.device_class || 'unknown',
                timestamp: entity.last_changed,
                severity: this.getEventSeverity(entity.entity_id, entity.state),
                description: this.generateEventDescription(entity)
            }))
            .sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
    }

    // Helper methods
    isDevice(entityId) {
        return entityId.startsWith('light.') ||
               entityId.startsWith('switch.') ||
               entityId.startsWith('sensor.') ||
               entityId.startsWith('binary_sensor.') ||
               entityId.startsWith('climate.') ||
               entityId.startsWith('media_player.') ||
               entityId.startsWith('cover.') ||
               entityId.startsWith('fan.') ||
               entityId.startsWith('lock.') ||
               entityId.startsWith('camera.');
    }

    isSecurityEntity(entityId) {
        return entityId.includes('door') ||
               entityId.includes('window') ||
               entityId.includes('motion') ||
               entityId.includes('smoke') ||
               entityId.includes('leak') ||
               entityId.includes('security') ||
               entityId.includes('alarm') ||
               entityId.includes('camera');
    }

    getDeviceType(entityId, deviceClass) {
        if (entityId.startsWith('light.')) return 'light';
        if (entityId.startsWith('switch.')) return 'switch';
        if (entityId.startsWith('climate.')) return 'thermostat';
        if (entityId.startsWith('media_player.')) return 'media';
        if (entityId.startsWith('sensor.')) return 'sensor';
        if (entityId.startsWith('binary_sensor.')) return 'sensor';
        if (entityId.startsWith('camera.')) return 'camera';
        if (entityId.startsWith('lock.')) return 'lock';
        if (entityId.startsWith('cover.')) return 'cover';
        return deviceClass || 'unknown';
    }

    getDeviceRoom(entityId) {
        if (entityId.includes('living_room')) return 'Living Room';
        if (entityId.includes('bedroom')) return 'Bedroom';
        if (entityId.includes('kitchen')) return 'Kitchen';
        if (entityId.includes('bathroom')) return 'Bathroom';
        if (entityId.includes('hallway')) return 'Hallway';
        if (entityId.includes('garage')) return 'Garage';
        if (entityId.includes('outdoor')) return 'Outdoor';
        return 'Unknown';
    }

    getDeviceIcon(entityId, deviceClass) {
        if (entityId.startsWith('light.')) return '💡';
        if (entityId.startsWith('switch.')) return '🔌';
        if (entityId.startsWith('climate.')) return '🌡️';
        if (entityId.startsWith('media_player.')) return '🔊';
        if (entityId.startsWith('camera.')) return '📹';
        if (entityId.startsWith('lock.')) return '🔒';
        if (entityId.startsWith('sensor.')) return '📡';
        return '🏠';
    }

    getEventType(entityId, state) {
        if (entityId.includes('motion')) return 'motion_detected';
        if (entityId.includes('door') && state === 'on') return 'door_opened';
        if (entityId.includes('door') && state === 'off') return 'door_closed';
        if (entityId.includes('window') && state === 'on') return 'window_opened';
        if (entityId.includes('window') && state === 'off') return 'window_closed';
        if (entityId.includes('smoke') && state === 'on') return 'smoke_detected';
        if (entityId.includes('leak') && state === 'on') return 'water_leak_detected';
        if (entityId.includes('alarm') && state === 'on') return 'alarm_triggered';
        return 'state_changed';
    }

    getEventSeverity(eventType) {
        if (eventType === 'motion_detected') return 'medium';
        if (eventType === 'door_opened') return 'low';
        if (eventType === 'door_closed') return 'low';
        if (eventType === 'window_opened') return 'medium';
        if (eventType === 'window_closed') return 'low';
        if (eventType === 'smoke_detected') return 'high';
        if (eventType === 'water_leak_detected') return 'high';
        if (eventType === 'alarm_triggered') return 'critical';
        return 'medium';
    }

    generateEventDescription(entity) {
        const eventType = this.getEventType(entity.entity_id, entity.state);
        const name = entity.attributes?.friendly_name || entity.entity_id;
        
        switch (eventType) {
            case 'motion_detected': return `Motion detected by ${name}`;
            case 'door_opened': return `${name} opened`;
            case 'door_closed': return `${name} closed`;
            case 'window_opened': return `${name} opened`;
            case 'window_closed': return `${name} closed`;
            case 'smoke_detected': return `Smoke detected by ${name}`;
            case 'water_leak_detected': return `Water leak detected by ${name}`;
            case 'alarm_triggered': return `Alarm triggered by ${name}`;
            default: return `${name}: ${eventType}`;
        }
    }

    // UI Update methods
    updateConnectionStatus(status, type) {
        const statusElement = document.getElementById('connection-status');
        const statusText = document.getElementById('connection-status-text');
        
        if (statusElement && statusText) {
            statusElement.className = `status-${type}`;
            statusText.textContent = status;
        }
    }

    updateDevicesDisplay() {
        const devicesContainer = document.getElementById('devices-container');
        if (!devicesContainer) return;

        const devicesHtml = this.devices.map(device => `
            <div class="device-card ${device.state === 'on' ? 'device-on' : 'device-off'}">
                <div class="device-header">
                    <span class="device-icon">${device.icon}</span>
                    <div class="device-info">
                        <h3>${device.name}</h3>
                        <p class="device-state">${device.state}</p>
                        <p class="device-room">${device.room}</p>
                    </div>
                </div>
                <div class="device-controls">
                    <button onclick="haIntegration.toggleDevice('${device.id}')" class="btn-toggle">
                        ${device.state === 'on' ? 'Turn Off' : 'Turn On'}
                    </button>
                    ${device.attributes.brightness !== undefined ? `
                        <input type="range" min="0" max="100" value="${device.attributes.brightness}" 
                               onchange="haIntegration.setBrightness('${device.id}', this.value)" 
                               class="brightness-slider">
                    ` : ''}
                </div>
            </div>
        `).join('');

        devicesContainer.innerHTML = devicesHtml;
    }

    updateEnergyDisplay() {
        const energyContainer = document.getElementById('energy-container');
        if (!energyContainer || !this.energyData) return;

        const energyHtml = `
            <div class="energy-overview">
                <div class="energy-card">
                    <h3>⚡ Power Consumption</h3>
                    <div class="energy-value">${this.energyData.totalPowerConsumption.toFixed(2)} W</div>
                    <div class="energy-label">Current Usage</div>
                </div>
                <div class="energy-card">
                    <h3>📊 Total Energy</h3>
                    <div class="energy-value">${this.energyData.totalEnergyConsumption.toFixed(2)} kWh</div>
                    <div class="energy-label">Total Consumption</div>
                </div>
                <div class="energy-devices">
                    <h4>Energy Devices</h4>
                    ${this.energyData.devices.map(device => `
                        <div class="energy-device">
                            <span class="device-name">${device.name}</span>
                            <span class="device-value">${device.value} ${device.unit}</span>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;

        energyContainer.innerHTML = energyHtml;
    }

    updateSecurityDisplay() {
        const securityContainer = document.getElementById('security-container');
        if (!securityContainer) return;

        const securityHtml = `
            <div class="security-overview">
                <div class="security-header">
                    <h3>🔒 Security Events</h3>
                    <span class="event-count">${this.securityEvents.length} recent events</span>
                </div>
                <div class="security-events">
                    ${this.securityEvents.map(event => `
                        <div class="security-event ${event.severity}">
                            <div class="event-header">
                                <span class="event-icon">${this.getEventIcon(event.eventType)}</span>
                                <div class="event-info">
                                    <h4>${event.name}</h4>
                                    <p class="event-description">${event.description}</p>
                                    <p class="event-time">${new Date(event.timestamp).toLocaleString()}</p>
                                </div>
                            </div>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;

        securityContainer.innerHTML = securityHtml;
    }

    getEventIcon(eventType) {
        const icons = {
            'motion_detected': '🚶',
            'door_opened': '🚪',
            'door_closed': '🚪',
            'window_opened': '🪟',
            'window_closed': '🪟',
            'smoke_detected': '🔥',
            'water_leak_detected': '💧',
            'alarm_triggered': '🚨',
            'alarm_disarmed': '🔓'
        };
        return icons[eventType] || '📢';
    }

    // Device control methods
    async toggleDevice(deviceId) {
        try {
            const device = this.devices.find(d => d.id === deviceId);
            if (!device) return;

            const newState = device.state === 'on' ? 'off' : 'on';
            const service = device.id.startsWith('light.') ? 'light.toggle' : 'homeassistant.toggle';
            
            console.log(`🔄 Toggling ${deviceId} to ${newState}`);

            const response = await fetch(`${this.haUrl}/api/services/${service}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    entity_id: deviceId,
                    service: service,
                }),
            });

            if (response.ok) {
                console.log(`✅ Successfully controlled ${deviceId}`);
                // Refresh device data
                await this.loadDevices();
            } else {
                console.error(`❌ Failed to control ${deviceId}`);
            }
        } catch (error) {
            console.error(`❌ Error controlling ${deviceId}:`, error);
        }
    }

    async setBrightness(deviceId, brightness) {
        try {
            const service = 'light.turn_on';
            
            console.log(`💡 Setting brightness for ${deviceId} to ${brightness}`);

            const response = await fetch(`${this.haUrl}/api/services/light.turn_on`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    entity_id: deviceId,
                    service: service,
                    service_data: {
                        brightness: parseInt(brightness)
                    }
                }),
            });

            if (response.ok) {
                console.log(`✅ Successfully set brightness for ${deviceId}`);
                await this.loadDevices();
            } else {
                console.error(`❌ Failed to set brightness for ${deviceId}`);
            }
        } catch (error) {
            console.error(`❌ Error setting brightness for ${deviceId}:`, error);
        }
    }

    // Real-time updates
    startRealTimeUpdates() {
        console.log('🔄 Starting real-time updates...');
        // Update every 5 seconds
        setInterval(async () => {
            if (this.isConnected) {
                await this.loadDevices();
                await this.loadEnergyData();
                await this.loadSecurityEvents();
            }
        }, 5000);
    }

    showConnectionError() {
        const container = document.getElementById('main-container');
        if (container) {
            container.innerHTML = `
                <div class="error-container">
                    <div class="error-card">
                        <h2>❌ Connection Error</h2>
                        <p>Failed to connect to Home Assistant. Please check:</p>
                        <ul>
                            <li>Home Assistant URL is correct</li>
                            <li>Access token is valid</li>
                            <li>Home Assistant is running and accessible</li>
                            <li>Network connection is working</li>
                        </ul>
                        <button onclick="location.reload()" class="btn-retry">Retry Connection</button>
                    </div>
                </div>
            `;
        }
    }
}

// Initialize the integration
const haIntegration = new HomeAssistantWebIntegration();
