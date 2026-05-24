package com.scms.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public String index() {
        return "智能学习助手后端运行成功";
    }

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "backend is running");
        result.put("status", "ok");
        return result;
    }
}