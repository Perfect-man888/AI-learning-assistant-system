package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.AiGenerateQuestionRequest;
import com.scms.learningassistantclient.model.Question;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.List;

public class AiQuestionApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Question> generateQuestions(AiGenerateQuestionRequest request) {
        try {
            String jsonBody = objectMapper.writeValueAsString(request);

            String resultJson = ApiClient.post(
                    AppConfig.getAiGenerateQuestionsUrl(),
                    jsonBody
            );

            return objectMapper.readValue(resultJson, new TypeReference<List<Question>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AI生成题目失败：" + e.getMessage());
        }
    }
}