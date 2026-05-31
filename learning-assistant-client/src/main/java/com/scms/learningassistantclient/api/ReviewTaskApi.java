package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.model.LearningTask;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.HashMap;
import java.util.Map;

public class ReviewTaskApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LearningTask generateReviewTask(Long studentId, Long courseId, Integer count) {
        try {
            Map<String, Object> body = new HashMap<>();

            body.put("studentId", studentId);
            body.put("courseId", courseId);
            body.put("count", count == null ? 5 : count);

            String jsonBody = objectMapper.writeValueAsString(body);

            String resultJson = ApiClient.post("/api/review-tasks/generate", jsonBody);

            return objectMapper.readValue(resultJson, LearningTask.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成个性化复习任务失败：" + e.getMessage());
        }
    }
}