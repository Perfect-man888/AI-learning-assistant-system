package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.model.WrongQuestion;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.List;

public class WrongQuestionApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<WrongQuestion> getWrongQuestionsByStudentId(Long studentId) {
        try {
            String json = ApiClient.get("/api/wrong-questions/student/" + studentId);

            return objectMapper.readValue(json, new TypeReference<List<WrongQuestion>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取错题本失败：" + e.getMessage());
        }
    }

    public List<WrongQuestion> getWrongQuestionsByStudentIdAndCourseId(Long studentId, Long courseId) {
        try {
            String json = ApiClient.get("/api/wrong-questions/student/" + studentId + "/course/" + courseId);

            return objectMapper.readValue(json, new TypeReference<List<WrongQuestion>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取课程错题本失败：" + e.getMessage());
        }
    }

    public String markMastered(Long wrongQuestionId) {
        try {
            return ApiClient.put("/api/wrong-questions/" + wrongQuestionId + "/mastered", "{}");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("标记掌握失败：" + e.getMessage());
        }
    }
}