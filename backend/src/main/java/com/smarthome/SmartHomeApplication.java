package com.smarthome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@EnableMongoRepositories
@EnableScheduling
@RestController
public class SmartHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeApplication.class, args);
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Smart Home Backend is running!", "status", "OK");
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("message", "Test endpoint working!", "mongodb", "connected");
    }

    @PostMapping("/register-simple")
    public Map<String, Object> registerSimple(@RequestBody Map<String, Object> data) {
        System.out.println("MAIN CLASS REGISTRATION: " + data);
        return Map.of("message", "Registration in main class working!", "received", data);
    }
}
