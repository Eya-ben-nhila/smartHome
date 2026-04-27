package com.smarthome.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "This is a public endpoint");
    }

    @GetMapping("/secure")
    public Map<String, String> secureEndpoint() {
        return Map.of("message", "This is a secure endpoint");
    }
}
