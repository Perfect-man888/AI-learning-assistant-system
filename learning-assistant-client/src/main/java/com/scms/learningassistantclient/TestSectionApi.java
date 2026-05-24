package com.scms.learningassistantclient;

import com.scms.learningassistantclient.api.SectionApi;
import com.scms.learningassistantclient.model.CourseSection;

import java.util.List;

public class TestSectionApi {

    public static void main(String[] args) {
        SectionApi sectionApi = new SectionApi();

        Long chapterId = 3L;

        List<CourseSection> sections = sectionApi.getSectionsByChapterId(chapterId);

        System.out.println("小节数量：" + sections.size());

        for (CourseSection section : sections) {
            System.out.println(
                    section.getId() + " - "
                            + section.getSectionTitle()
                            + " - 知识点：" + section.getKnowledgePoints()
                            + " - 排序：" + section.getSortOrder()
            );
        }
    }
}