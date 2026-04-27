package com.smarthome.controller;

import com.smarthome.entity.Automation;
import com.smarthome.entity.TriggerType;
import com.smarthome.service.AutomationService;
import com.smarthome.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/automations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AutomationController {

    @Autowired
    private AutomationService automationService;

    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Automation>> getAllAutomations() {
        String userId = currentUserService.getCurrentUserId();
        List<Automation> automations = automationService.getAllAutomationsByUserId(userId);
        return ResponseEntity.ok(automations);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Automation>> getActiveAutomations() {
        String userId = currentUserService.getCurrentUserId();
        List<Automation> automations = automationService.getActiveAutomationsByUserId(userId);
        return ResponseEntity.ok(automations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Automation> getAutomationById(@PathVariable String id) {
        Optional<Automation> automation = automationService.getAutomationById(id, currentUserService.getCurrentUserId());
        return automation.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Automation> createAutomation(@Valid @RequestBody Automation automation) {
        automation.setUserId(currentUserService.getCurrentUserId());
        Automation createdAutomation = automationService.createAutomation(automation);
        return ResponseEntity.ok(createdAutomation);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Automation> updateAutomation(@PathVariable String id, @Valid @RequestBody Automation automation) {
        try {
            Automation updatedAutomation = automationService.updateAutomation(id, currentUserService.getCurrentUserId(), automation);
            return ResponseEntity.ok(updatedAutomation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAutomation(@PathVariable String id) {
        automationService.deleteAutomation(id, currentUserService.getCurrentUserId());
        return ResponseEntity.ok(Map.of("message", "Automation deleted successfully"));
    }

    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Automation> toggleAutomationStatus(@PathVariable String id) {
        try {
            Automation automation = automationService.toggleAutomationStatus(id, currentUserService.getCurrentUserId());
            return ResponseEntity.ok(automation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/execute")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> executeAutomationManually(@PathVariable String id) {
        try {
            automationService.executeAutomationManually(id, currentUserService.getCurrentUserId());
            return ResponseEntity.ok(Map.of("message", "Automation executed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to execute automation"));
        }
    }

    @GetMapping("/trigger/{triggerType}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Automation>> getAutomationsByTriggerType(@PathVariable TriggerType triggerType) {
        List<Automation> automations = automationService.getAutomationsByTriggerType(
                currentUserService.getCurrentUserId(),
                triggerType
        );
        return ResponseEntity.ok(automations);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAutomationStats() {
        String userId = currentUserService.getCurrentUserId();
        Long activeCount = automationService.countActiveAutomationsByUserId(userId);
        List<Automation> allAutomations = automationService.getAllAutomationsByUserId(userId);
        List<Automation> recentExecutions = automationService.getRecentlyExecutedAutomations(userId, java.time.LocalDateTime.now().minusDays(7));
        
        return ResponseEntity.ok(Map.of(
                "totalAutomations", allAutomations.size(),
                "activeAutomations", activeCount,
                "inactiveAutomations", allAutomations.size() - activeCount,
                "recentExecutions", recentExecutions.size()
        ));
    }
}
