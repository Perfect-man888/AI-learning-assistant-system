package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.model.LearningTaskQuestion;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.HashMap;
import java.util.Map;

public class TaskQuestionApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LearningTaskQuestion addQuestionToTask(Long taskId, Long questionId, Integer sortOrder) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("taskId", taskId);
            body.put("questionId", questionId);
            body.put("sortOrder", sortOrder == null ? 0 : sortOrder);

            String jsonBody = objectMapper.writeValueAsString(body);

            String resultJson = ApiClient.post("/api/task-questions", jsonBody);

            return objectMapper.readValue(resultJson, LearningTaskQuestion.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("题目加入任务失败：" + e.getMessage());
        }
    }
}