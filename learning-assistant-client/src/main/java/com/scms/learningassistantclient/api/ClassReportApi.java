package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.ClassLearningReport;
import com.scms.learningassistantclient.util.ApiClient;

public class ClassReportApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClassLearningReport getClassLearningReport(Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getClassLearningReportUrl(courseId));

            return objectMapper.readValue(json, ClassLearningReport.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取班级综合学习报告失败：" + e.getMessage());
        }
    }
}