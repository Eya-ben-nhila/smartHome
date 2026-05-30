package com.smarthome.config;

import com.smarthome.entity.Role;
import com.smarthome.entity.User;
import com.smarthome.repository.UserRepository;
import com.smarthome.service.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public AdminAccountInitializer(UserRepository userRepository, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@smartmonitor.com";
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User();
        admin.setFullName("SmartMonitor Admin");
        admin.setEmail(adminEmail);
        admin.setPassword("admin123");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setEmailVerified(true);
        userDetailsService.createUser(admin);
    }
}
