package com.smarthome.service;

import com.smarthome.entity.Automation;
import com.smarthome.entity.TriggerType;
import com.smarthome.repository.AutomationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AutomationService {

    @Autowired
    private AutomationRepository automationRepository;

    @Autowired
    private com.smarthome.automation.AutomationEngine automationEngine;

    public List<Automation> getAllAutomationsByUserId(String userId) {
        return automationRepository.findByUserId(userId);
    }

    public List<Automation> getActiveAutomationsByUserId(String userId) {
        return automationRepository.findByUserIdAndIsActive(userId, true);
    }

    public Optional<Automation> getAutomationById(String id, String userId) {
        return automationRepository.findByIdAndUserId(id, userId);
    }

    public Automation createAutomation(Automation automation) {
        automation.setExecutionCount(0L);
        automation.setLastExecuted(null);
        return automationRepository.save(automation);
    }

    public Automation updateAutomation(String id, String userId, Automation automationDetails) {
        Optional<Automation> automationOptional = automationRepository.findByIdAndUserId(id, userId);
        if (automationOptional.isPresent()) {
            Automation automation = automationOptional.get();
            automation.setAutomationName(automationDetails.getAutomationName());
            automation.setTriggerType(automationDetails.getTriggerType());
            automation.setTriggerData(automationDetails.getTriggerData());
            automation.setActionType(automationDetails.getActionType());
            automation.setActionData(automationDetails.getActionData());
            automation.setActive(automationDetails.isActive());
            return automationRepository.save(automation);
        }
        throw new RuntimeException("Automation not found with id: " + id);
    }

    public void deleteAutomation(String id, String userId) {
        Automation automation = automationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Automation not found with id: " + id));
        automationRepository.delete(automation);
    }

    public Automation toggleAutomationStatus(String id, String userId) {
        Optional<Automation> automationOptional = automationRepository.findByIdAndUserId(id, userId);
        if (automationOptional.isPresent()) {
            Automation automation = automationOptional.get();
            automation.setActive(!automation.isActive());
            return automationRepository.save(automation);
        }
        throw new RuntimeException("Automation not found with id: " + id);
    }

    public void executeAutomationManually(String id, String userId) {
        Optional<Automation> automationOptional = automationRepository.findByIdAndUserId(id, userId);
        if (automationOptional.isPresent()) {
            Automation automation = automationOptional.get();
            automationEngine.executeAutomation(automation);
        }
    }

    public List<Automation> getAutomationsByTriggerType(String userId, TriggerType triggerType) {
        return automationRepository.findByUserIdAndTriggerTypeAndIsActive(userId, triggerType, true);
    }

    public Long countActiveAutomationsByUserId(String userId) {
        return automationRepository.countActiveAutomationsByUserId(userId);
    }

    public List<Automation> getRecentlyExecutedAutomations(String userId, LocalDateTime since) {
        return automationRepository.findRecentlyExecutedAutomations(userId, since);
    }
}
