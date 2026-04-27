package com.smarthome.repository;

import com.smarthome.entity.EnergyReading;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyReadingRepository extends MongoRepository<EnergyReading, String> {
    
    List<EnergyReading> findByDeviceIdOrderByReadingTimeDesc(String deviceId);
    
    List<EnergyReading> findByDeviceIdAndReadingTimeBetweenOrderByReadingTimeDesc(
            String deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("{ 'deviceId': ?0, 'readingTime': { $gte: ?1 } }")
    List<EnergyReading> findRecentReadingsByDeviceId(String deviceId, LocalDateTime since);
    
    @Query("{ 'deviceId': ?0, 'readingTime': { $gte: ?1, $lte: ?2 } }")
    Double getTotalEnergyConsumption(String deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'deviceId': ?0, 'readingTime': { $gte: ?1, $lte: ?2 } }")
    Double getAveragePowerUsage(String deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'deviceId': ?0, 'readingTime': { $gte: ?1, $lte: ?2 } }")
    Double getPeakPowerUsage(String deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'userId': ?0, 'readingTime': { $gte: ?1, $lte: ?2 } }")
    Double getTotalEnergyConsumptionByUserId(String userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'userId': ?0, 'readingTime': { $gte: ?1, $lte: ?2 } }")
    Double getTotalCostByUserId(String userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'userId': ?0, 'readingTime': { $gte: ?1 } }")
    List<EnergyReading> findRecentReadingsByUserId(String userId, LocalDateTime since);
    
    @Query(value = "{ 'userId': ?0, 'readingTime': { $gte: ?1 } }", count = true)
    Long countReadingsByUserIdSince(String userId, LocalDateTime since);
}
