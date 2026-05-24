package com.scms.learning.dto;

public class AiGenerateQuestionRequest {

    private Long courseId;
    private Long chapterId;
    private Long sectionId;

    private String knowledgePoint;
    private String questionType;
    private String difficulty;
    private Integer count;

    public AiGenerateQuestionRequest() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Integer getCount() {
        return count;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}