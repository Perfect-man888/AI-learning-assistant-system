package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.model.StudentReport;
import com.scms.learningassistantclient.util.ApiClient;

public class ReportApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StudentReport getStudentCourseReport(Long studentId, Long courseId) {
        try {
            String json = ApiClient.get(
                    "/api/reports/student/" + studentId + "/course/" + courseId
            );

            return objectMapper.readValue(json, StudentReport.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取学习报告失败：" + e.getMessage());
        }
    }
}