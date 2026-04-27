package com.smarthome.repository;

import com.smarthome.entity.Automation;
import com.smarthome.entity.TriggerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutomationRepository extends MongoRepository<Automation, String> {
    
    List<Automation> findByUserId(String userId);
    
    Optional<Automation> findByIdAndUserId(String id, String userId);
    
    List<Automation> findByIsActive(boolean isActive);
    
    List<Automation> findByUserIdAndIsActive(String userId, boolean isActive);
    
    List<Automation> findByTriggerType(TriggerType triggerType);
    
    List<Automation> findByTriggerTypeAndIsActive(TriggerType triggerType, boolean isActive);
    
    List<Automation> findByUserIdAndTriggerTypeAndIsActive(String userId, TriggerType triggerType, boolean isActive);
    
    @Query(value = "{ 'userId': ?0, 'isActive': true }", count = true)
    Long countActiveAutomationsByUserId(String userId);
    
    @Query("{ 'userId': ?0, 'lastExecuted': { $gte: ?1 } }")
    List<Automation> findRecentlyExecutedAutomations(String userId, java.time.LocalDateTime since);
}
