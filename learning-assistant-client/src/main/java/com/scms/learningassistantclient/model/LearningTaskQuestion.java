package com.scms.learningassistantclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningTaskQuestion {

    private Long id;
    private Long taskId;
    private Long questionId;
    private Integer sortOrder;
    private String createTime;

    public LearningTaskQuestion() {
    }

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}