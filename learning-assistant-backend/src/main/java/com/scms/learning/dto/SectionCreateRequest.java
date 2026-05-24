package com.scms.learning.dto;


public class SectionCreateRequest {

    private String sectionTitle;
    private String content;
    private String knowledgePoints;
    private Integer sortOrder;

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
}