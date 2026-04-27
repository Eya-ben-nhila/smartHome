package com.smarthome.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CompleteController {

    // === AUTHENTIFICATION ===
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "Backend is working!", "version", "1.0.0"));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        
        if (email != null && password != null && password.length() >= 6) {
            return ResponseEntity.ok(Map.of(
                "token", "mock-jwt-token-" + System.currentTimeMillis(),
                "user", Map.of(
                    "id", "user-123",
                    "email", email,
                    "fullName", "Utilisateur Test",
                    "role", "USER"
                )
            ));
        }
        
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registerData) {
        String fullName = registerData.get("fullName");
        String email = registerData.get("email");
        String password = registerData.get("password");
        
        if (fullName != null && email != null && password != null && password.length() >= 6) {
            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", "user-" + System.currentTimeMillis(),
                "email", email
            ));
        }
        
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid registration data"));
    }

    // === EXPORT CSV/PDF ===
    
    @GetMapping("/export/csv/users")
    public ResponseEntity<byte[]> exportUsersToCsv() {
        try {
            String csvData = generateUsersCsv();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "users.csv");
            headers.setContentLength(csvData.length());
            
            return ResponseEntity.ok().headers(headers).body(csvData.getBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/export/pdf/users")
    public ResponseEntity<byte[]> exportUsersToPdf() {
        try {
            String pdfData = generateUsersCsv(); // Simplifié: CSV avec extension PDF
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "users.pdf");
            headers.setContentLength(pdfData.length());
            
            return ResponseEntity.ok().headers(headers).body(pdfData.getBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/export/csv/devices")
    public ResponseEntity<byte[]> exportDevicesToCsv() {
        try {
            String csvData = generateDevicesCsv();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "devices.csv");
            headers.setContentLength(csvData.length());
            
            return ResponseEntity.ok().headers(headers).body(csvData.getBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/export/csv/energy")
    public ResponseEntity<byte[]> exportEnergyDataToCsv() {
        try {
            String csvData = generateEnergyCsv();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "energy-data.csv");
            headers.setContentLength(csvData.length());
            
            return ResponseEntity.ok().headers(headers).body(csvData.getBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
        }
    }

    // === ALERTES ===
    
    @GetMapping("/alerts/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserAlerts(@PathVariable String userId) {
        return ResponseEntity.ok(generateMockAlerts());
    }

    @GetMapping("/alerts/user/{userId}/active")
    public ResponseEntity<List<Map<String, Object>>> getActiveAlerts(@PathVariable String userId) {
        List<Map<String, Object>> allAlerts = generateMockAlerts();
        List<Map<String, Object>> activeAlerts = new ArrayList<>();
        
        for (Map<String, Object> alert : allAlerts) {
            if ((Boolean) alert.get("isActive")) {
                activeAlerts.add(alert);
            }
        }
        
        return ResponseEntity.ok(activeAlerts);
    }

    @GetMapping("/alerts/user/{userId}/unread")
    public ResponseEntity<List<Map<String, Object>>> getUnreadAlerts(@PathVariable String userId) {
        List<Map<String, Object>> allAlerts = generateMockAlerts();
        List<Map<String, Object>> unreadAlerts = new ArrayList<>();
        
        for (Map<String, Object> alert : allAlerts) {
            if (!(Boolean) alert.get("isRead")) {
                unreadAlerts.add(alert);
            }
        }
        
        return ResponseEntity.ok(unreadAlerts);
    }

    @PostMapping("/alerts/mark-read/{alertId}")
    public ResponseEntity<Map<String, String>> markAlertAsRead(@PathVariable String alertId) {
        return ResponseEntity.ok(Map.of("message", "Alert marked as read"));
    }

    @PostMapping("/alerts/resolve/{alertId}")
    public ResponseEntity<Map<String, String>> resolveAlert(@PathVariable String alertId) {
        return ResponseEntity.ok(Map.of("message", "Alert resolved successfully"));
    }

    @GetMapping("/alerts/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserAlertStats(@PathVariable String userId) {
        List<Map<String, Object>> alerts = generateMockAlerts();
        long unreadCount = alerts.stream().mapToLong(alert -> (Boolean) alert.get("isRead") ? 0 : 1).sum();
        long activeCount = alerts.stream().mapToLong(alert -> (Boolean) alert.get("isActive") ? 1 : 0).sum();
        
        return ResponseEntity.ok(Map.of(
            "unreadCount", unreadCount,
            "activeCount", activeCount,
            "totalAlerts", alerts.size()
        ));
    }

    // === CACHE REDIS (SIMULATION) ===
    
    @PostMapping("/cache/set")
    public ResponseEntity<Map<String, String>> setCache(@RequestBody Map<String, String> cacheData) {
        return ResponseEntity.ok(Map.of("message", "Data cached successfully"));
    }

    @GetMapping("/cache/get/{key}")
    public ResponseEntity<Map<String, Object>> getCache(@PathVariable String key) {
        return ResponseEntity.ok(Map.of(
            "key", key,
            "value", "cached-value-" + System.currentTimeMillis(),
            "timestamp", new Date()
        ));
    }

    // === MÉTHODES PRIVÉES ===
    
    private String generateUsersCsv() {
        return "ID,Full Name,Email,Role,Created At\n" +
               "1,Jean Dupont,jean.dupont@example.com,USER,2024-01-01 10:00:00\n" +
               "2,Marie Curie,marie.curie@example.com,USER,2024-01-02 11:30:00\n" +
               "3,Pierre Martin,pierre.martin@example.com,USER,2024-01-03 14:15:00\n" +
               "4,Sophie Durand,sophie.durand@example.com,ADMIN,2024-01-04 09:45:00\n";
    }

    private String generateDevicesCsv() {
        return "ID,Name,Type,Status,Location,Created At\n" +
               "1,Thermostat Salon,Climate,ON,Living Room,2024-01-01 10:00:00\n" +
               "2,Lumière Chambre,Light,OFF,Bedroom,2024-01-02 11:30:00\n" +
               "3,Caméra Entrée,Security,RECORDING,Entrance,2024-01-03 14:15:00\n" +
               "4,Prise Cuisine,Power,ON,Kitchen,2024-01-04 09:45:00\n";
    }

    private String generateEnergyCsv() {
        return "ID,Device ID,Value,Unit,Timestamp\n" +
               "1,device-123,15.50,kWh,2024-01-01T10:00:00\n" +
               "2,device-456,12.30,kWh,2024-01-01T11:00:00\n" +
               "3,device-789,18.75,kWh,2024-01-01T12:00:00\n" +
               "4,device-123,22.10,kWh,2024-01-01T13:00:00\n";
    }

    private List<Map<String, Object>> generateMockAlerts() {
        return Arrays.asList(
            Map.of(
                "id", "1",
                "title", "Appareil Hors Ligne",
                "message", "Le thermostat du salon n'est plus connecté",
                "type", "DEVICE_OFFLINE",
                "severity", "HIGH",
                "isActive", true,
                "isRead", false,
                "createdAt", new Date().toString()
            ),
            Map.of(
                "id", "2",
                "title", "Consommation Élevée",
                "message", "Le système a consommé 25% plus que d'habitude",
                "type", "HIGH_ENERGY_CONSUMPTION",
                "severity", "MEDIUM",
                "isActive", true,
                "isRead", false,
                "createdAt", new Date().toString()
            ),
            Map.of(
                "id", "3",
                "title", "Alerte Sécurité",
                "message", "Mouvement détecté à l'entrée principale",
                "type", "SECURITY_ALERT",
                "severity", "CRITICAL",
                "isActive", true,
                "isRead", true,
                "createdAt", new Date().toString()
            )
        );
    }
}
