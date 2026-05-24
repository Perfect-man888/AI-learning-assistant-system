package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.StudyProgress;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scms.learningassistantclient.model.StudentProgressSummary;


public class StudyProgressApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StudyProgress completeSection(
            Long courseId,
            Long chapterId,
            Long sectionId,
            Long studentId
    ) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("courseId", courseId);
            body.put("chapterId", chapterId);
            body.put("sectionId", sectionId);
            body.put("studentId", studentId);
            body.put("status", "completed");
            body.put("progressPercent", 100);

            String jsonBody = objectMapper.writeValueAsString(body);

            String resultJson = ApiClient.post(AppConfig.getStudyProgressSectionUrl(), jsonBody);

            return objectMapper.readValue(resultJson, StudyProgress.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("记录学习进度失败：" + e.getMessage());
        }
    }

    public List<StudyProgress> getStudentCourseProgress(Long studentId, Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getStudentCourseProgressUrl(studentId, courseId));

            return objectMapper.readValue(json, new TypeReference<List<StudyProgress>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取学习进度失败：" + e.getMessage());
        }
    }

    public List<StudentProgressSummary> getCourseProgressSummary(Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getCourseProgressSummaryUrl(courseId));

            return objectMapper.readValue(json, new TypeReference<List<StudentProgressSummary>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取课程学习进度统计失败：" + e.getMessage());
        }
    }

}