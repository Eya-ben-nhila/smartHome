package com.smarthome.repository;

import com.smarthome.entity.Device;
import com.smarthome.entity.DeviceType;
import com.smarthome.entity.DeviceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
    
    List<Device> findByUserId(String userId);
    
    Optional<Device> findByIdAndUserId(String id, String userId);
    
    Optional<Device> findByMacAddress(String macAddress);
    
    List<Device> findByUserIdAndDeviceType(String userId, DeviceType deviceType);
    
    List<Device> findByDeviceStatus(DeviceStatus status);
    
    @Query("{ 'userId': ?0, 'isOnline': true }")
    List<Device> findOnlineDevicesByUserId(String userId);
    
    @Query(value = "{ 'userId': ?0, 'isOnline': true }", count = true)
    Long countOnlineDevicesByUserId(String userId);
    
    @Query("{ 'userId': ?0, 'deviceType': ?1, 'isOnline': true }")
    List<Device> findOnlineDevicesByType(String userId, DeviceType deviceType);
    
    // IoT-specific queries for MongoDB
    @Query("{ 'tags': { $in: ?0 } }")
    List<Device> findByTags(List<String> tags);
    
    @Query("{ 'batteryLevel': { $lt: ?0 } }")
    List<Device> findDevicesWithLowBattery(int threshold);
    
    @Query("{ 'lastSeen': { $lt: ?0 } }")
    List<Device> findOfflineDevicesSince(LocalDateTime since);
    
    @Query("{ 'deviceType': ?0, 'userId': ?1 }")
    List<Device> findActiveDevicesByType(DeviceType deviceType, String userId);
    
    // Methods for testing without user authentication
    List<Device> findByOnline(boolean isOnline);
    
    List<Device> findByDeviceType(DeviceType deviceType);
    
    @Query(value = "{ 'isOnline': true }", count = true)
    Long countByOnline(boolean isOnline);
}
