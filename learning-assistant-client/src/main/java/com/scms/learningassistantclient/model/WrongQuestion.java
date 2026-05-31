package com.scms.learningassistantclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrongQuestion {

    private Long id;
    private Long studentId;
    private Long questionId;
    private Long courseId;
    private Long chapterId;
    private Long sectionId;
    private Long knowledgePointId;
    private String knowledgePointName;
    private Integer wrongCount;
    private String lastWrongTime;
    private Integer isMastered;
    private String createTime;
    private String updateTime;

    public WrongQuestion() {
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getQuestionId() {
        return questionId;
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

    public Long getKnowledgePointId() {
        return knowledgePointId;
    }

    public String getKnowledgePointName() {
        return knowledgePointName;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public String getLastWrongTime() {
        return lastWrongTime;
    }

    public Integer getIsMastered() {
        return isMastered;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public void setKnowledgePointId(Long knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public void setKnowledgePointName(String knowledgePointName) {
        this.knowledgePointName = knowledgePointName;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public void setLastWrongTime(String lastWrongTime) {
        this.lastWrongTime = lastWrongTime;
    }

    public void setIsMastered(Integer isMastered) {
        this.isMastered = isMastered;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean mastered() {
        return isMastered != null && isMastered == 1;
    }

    @Override
    public String toString() {
        return "错题ID：" + id
                + "，题目ID：" + questionId
                + "，知识点：" + safe(knowledgePointName)
                + "，错误次数：" + safe(wrongCount)
                + "，状态：" + (mastered() ? "已掌握" : "未掌握");
    }

    private String safe(Object value) {
        return value == null ? "暂无" : String.valueOf(value);
    }
}