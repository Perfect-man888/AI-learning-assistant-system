package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.Question;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.List;

public class QuestionApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Question createQuestion(Question question) {
        try {
            String jsonBody = objectMapper.writeValueAsString(question);
            String resultJson = ApiClient.post(AppConfig.getCreateQuestionUrl(), jsonBody);

            return objectMapper.readValue(resultJson, Question.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增题目失败：" + e.getMessage());
        }
    }

    public List<Question> getQuestionsByCourseId(Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getQuestionListUrl(courseId));

            return objectMapper.readValue(json, new TypeReference<List<Question>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取课程题目失败：" + e.getMessage());
        }
    }

    public List<Question> getQuestionsByTaskId(Long taskId) {
        try {
            String json = ApiClient.get(AppConfig.getTaskQuestionListUrl(taskId));

            return objectMapper.readValue(json, new TypeReference<List<Question>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取任务题目失败：" + e.getMessage());
        }
    }
}