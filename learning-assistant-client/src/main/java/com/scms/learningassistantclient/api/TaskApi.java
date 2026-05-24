package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.LearningTask;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.List;

public class TaskApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LearningTask createTask(LearningTask task) {
        try {
            String jsonBody = objectMapper.writeValueAsString(task);
            String resultJson = ApiClient.post(AppConfig.getCreateTaskUrl(), jsonBody);

            return objectMapper.readValue(resultJson, LearningTask.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建测验任务失败：" + e.getMessage());
        }
    }

    public List<LearningTask> getTasksByCourseId(Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getTaskListUrl(courseId));

            return objectMapper.readValue(json, new TypeReference<List<LearningTask>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取测验任务失败：" + e.getMessage());
        }
    }

    public String deleteTask(Long taskId) {
        try {
            return ApiClient.delete(AppConfig.getDeleteTaskUrl(taskId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除测验任务失败：" + e.getMessage());
        }
    }



}