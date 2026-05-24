package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.CourseSection;
import com.scms.learningassistantclient.util.ApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionApi {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CourseSection> getSectionsByChapterId(Long chapterId) {
        try {
            String json = ApiClient.get(AppConfig.getSectionListUrl(chapterId));

            return objectMapper.readValue(json, new TypeReference<List<CourseSection>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取小节列表失败：" + e.getMessage());
        }
    }

    public CourseSection createSection(
            Long chapterId,
            String sectionTitle,
            String content,
            String knowledgePoints,
            Integer sortOrder
    ) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("sectionTitle", sectionTitle);
            body.put("content", content);
            body.put("knowledgePoints", knowledgePoints);
            body.put("sortOrder", sortOrder);

            String jsonBody = objectMapper.writeValueAsString(body);

            String resultJson = ApiClient.post(AppConfig.getSectionCreateUrl(chapterId), jsonBody);

            return objectMapper.readValue(resultJson, CourseSection.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增小节失败：" + e.getMessage());
        }
    }
}