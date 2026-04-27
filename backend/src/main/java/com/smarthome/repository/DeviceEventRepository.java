package com.smarthome.repository;

import com.smarthome.entity.DeviceEvent;
import com.smarthome.entity.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceEventRepository extends MongoRepository<DeviceEvent, String> {
    
    List<DeviceEvent> findByDeviceIdOrderByCreatedAtDesc(String deviceId);
    
    List<DeviceEvent> findByEventType(EventType eventType);
    
    List<DeviceEvent> findByDeviceIdAndEventType(String deviceId, EventType eventType);
    
    @Query("{ 'deviceId': ?0, 'createdAt': { $gte: ?1 } }")
    List<DeviceEvent> findRecentEventsByDeviceId(String deviceId, LocalDateTime since);
    
    @Query(value = "{ 'deviceId': ?0, 'createdAt': { $gte: ?1 } }", count = true)
    Long countEventsByDeviceIdSince(String deviceId, LocalDateTime since);
    
    @Query("{ 'device.userId': ?0, 'createdAt': { $gte: ?1 } }")
    List<DeviceEvent> findRecentEventsByUserId(String userId, LocalDateTime since);
    
    @Query("{ 'device.userId': ?0, 'eventType': { $in: ?1 } }")
    List<DeviceEvent> findEventsByUserIdAndTypes(String userId, List<EventType> eventTypes);
    
    @Query(value = "{ 'device.userId': ?0, 'eventType': ?1, 'createdAt': { $gte: ?2 } }", count = true)
    Long countEventsByUserIdAndTypeSince(String userId, EventType eventType, LocalDateTime since);
}
