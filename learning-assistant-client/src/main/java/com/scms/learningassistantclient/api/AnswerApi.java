package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.AnswerRecord;
import com.scms.learningassistantclient.util.ApiClient;
import com.scms.learningassistantclient.model.AnswerRecordDetail;


import java.util.List;

public class AnswerApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnswerRecord submitAnswer(AnswerRecord record) {
        try {
            String jsonBody = objectMapper.writeValueAsString(record);
            String resultJson = ApiClient.post(AppConfig.getSubmitAnswerUrl(), jsonBody);

            return objectMapper.readValue(resultJson, AnswerRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("提交答案失败：" + e.getMessage());
        }
    }

    public List<AnswerRecord> getTaskAnswerRecords(Long taskId) {
        try {
            String json = ApiClient.get(AppConfig.getTaskAnswerRecordsUrl(taskId));

            return objectMapper.readValue(json, new TypeReference<List<AnswerRecord>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取任务答题记录失败：" + e.getMessage());
        }
    }

    public List<AnswerRecord> getStudentAnswerRecords(Long studentId) {
        try {
            String json = ApiClient.get(AppConfig.getStudentAnswerRecordsUrl(studentId));

            return objectMapper.readValue(json, new TypeReference<List<AnswerRecord>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取学生答题记录失败：" + e.getMessage());
        }
    }

    public List<AnswerRecordDetail> getTaskAnswerRecordDetails(Long taskId) {
        try {
            String json = ApiClient.get(AppConfig.getTaskAnswerRecordDetailsUrl(taskId));

            return objectMapper.readValue(json, new TypeReference<List<AnswerRecordDetail>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取任务答题详细记录失败：" + e.getMessage());
        }
    }

    public List<AnswerRecordDetail> getStudentWrongAnswerDetails(Long studentId) {
        try {
            String json = ApiClient.get(AppConfig.getStudentWrongAnswerDetailsUrl(studentId));

            return objectMapper.readValue(json, new TypeReference<List<AnswerRecordDetail>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取学生错题本失败：" + e.getMessage());
        }
    }

    public List<AnswerRecordDetail> getStudentAnswerDetailsByCourse(Long studentId, Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getStudentAnswerDetailsByCourseUrl(studentId, courseId));

            return objectMapper.readValue(json, new TypeReference<List<AnswerRecordDetail>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取学生课程答题记录失败：" + e.getMessage());
        }
    }

    public List<AnswerRecordDetail> getStudentWrongAnswerDetailsByCourse(Long studentId, Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getStudentWrongAnswerDetailsByCourseUrl(studentId, courseId));

            return objectMapper.readValue(json, new TypeReference<List<AnswerRecordDetail>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取学生课程错题记录失败：" + e.getMessage());
        }
    }

}