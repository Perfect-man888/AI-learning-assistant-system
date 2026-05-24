package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.CourseChapter;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CourseChapter> getChaptersByCourseId(Long courseId) {
        try {
            String json = ApiClient.get(AppConfig.getChapterListUrl(courseId));

            return objectMapper.readValue(json, new TypeReference<List<CourseChapter>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取章节列表失败：" + e.getMessage());
        }
    }

    public CourseChapter createChapter(Long courseId, String chapterTitle, Integer sortOrder) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("chapterTitle", chapterTitle);
            body.put("sortOrder", sortOrder);

            String jsonBody = objectMapper.writeValueAsString(body);

            String resultJson = ApiClient.post(AppConfig.getChapterCreateUrl(courseId), jsonBody);

            return objectMapper.readValue(resultJson, CourseChapter.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增章节失败：" + e.getMessage());
        }
    }
}