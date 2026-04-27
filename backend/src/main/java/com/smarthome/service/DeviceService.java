package com.smarthome.service;

import com.smarthome.entity.Device;
import com.smarthome.entity.DeviceType;
import com.smarthome.entity.DeviceStatus;
import com.smarthome.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevicesByUserId(String userId) {
        return deviceRepository.findByUserId(userId);
    }

    public Optional<Device> getDeviceById(String deviceId, String userId) {
        return deviceRepository.findByIdAndUserId(deviceId, userId);
    }

    public Device createDevice(Device device) {
        device.setDeviceStatus(DeviceStatus.OFFLINE);
        device.setOnline(false);
        return deviceRepository.save(device);
    }

    public Device updateDevice(String deviceId, String userId, Device deviceDetails) {
        Optional<Device> deviceOptional = deviceRepository.findByIdAndUserId(deviceId, userId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            device.setDeviceName(deviceDetails.getDeviceName());
            device.setLocation(deviceDetails.getLocation());
            device.setModel(deviceDetails.getModel());
            device.setManufacturer(deviceDetails.getManufacturer());
            device.setFirmwareVersion(deviceDetails.getFirmwareVersion());
            return deviceRepository.save(device);
        }
        throw new RuntimeException("Device not found with id: " + deviceId);
    }

    public void deleteDevice(String deviceId, String userId) {
        Device device = deviceRepository.findByIdAndUserId(deviceId, userId)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        deviceRepository.delete(device);
    }

    public List<Device> getOnlineDevicesByUserId(String userId) {
        return deviceRepository.findOnlineDevicesByUserId(userId);
    }

    public List<Device> getDevicesByType(String userId, DeviceType deviceType) {
        return deviceRepository.findByUserIdAndDeviceType(userId, deviceType);
    }

    public Device updateDeviceStatus(String deviceId, String userId, DeviceStatus status) {
        Optional<Device> deviceOptional = deviceRepository.findByIdAndUserId(deviceId, userId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            device.setDeviceStatus(status);
            device.setOnline(status == DeviceStatus.ONLINE);
            device.setLastSeen(LocalDateTime.now());
            return deviceRepository.save(device);
        }
        throw new RuntimeException("Device not found with id: " + deviceId);
    }

    // Used by system integrations (e.g. MQTT) where no authenticated user context exists.
    public Device updateDeviceStatusFromSystem(String deviceId, DeviceStatus status) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            device.setDeviceStatus(status);
            device.setOnline(status == DeviceStatus.ONLINE);
            device.setLastSeen(LocalDateTime.now());
            return deviceRepository.save(device);
        }
        throw new RuntimeException("Device not found with id: " + deviceId);
    }

    public Long countOnlineDevicesByUserId(String userId) {
        return deviceRepository.countOnlineDevicesByUserId(userId);
    }

    public Optional<Device> findByMacAddress(String macAddress) {
        return deviceRepository.findByMacAddress(macAddress);
    }

    // Methods for testing without user authentication
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public List<Device> getOnlineDevices() {
        return deviceRepository.findByOnline(true);
    }

    public List<Device> getDevicesByType(DeviceType deviceType) {
        return deviceRepository.findByDeviceType(deviceType);
    }

    public Long countOnlineDevices() {
        return deviceRepository.countByOnline(true);
    }
}
