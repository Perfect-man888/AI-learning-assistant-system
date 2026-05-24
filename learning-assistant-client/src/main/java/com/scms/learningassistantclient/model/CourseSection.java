package com.scms.learningassistantclient.model;

public class CourseSection {

    private Long id;
    private Long chapterId;
    private String sectionTitle;
    private String content;
    private String knowledgePoints;
    private Integer sortOrder;

    public CourseSection() {
    }

    public CourseSection(Long id, Long chapterId, String sectionTitle, String content, String knowledgePoints, Integer sortOrder) {
        this.id = id;
        this.chapterId = chapterId;
        this.sectionTitle = sectionTitle;
        this.content = content;
        this.knowledgePoints = knowledgePoints;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getContent() {
        return content;
    }

    public String getKnowledgePoints() {
        return knowledgePoints;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setKnowledgePoints(String knowledgePoints) {
        this.knowledgePoints = knowledgePoints;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return sectionTitle;
    }
}