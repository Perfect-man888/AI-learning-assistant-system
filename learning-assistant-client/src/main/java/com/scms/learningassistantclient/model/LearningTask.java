package com.scms.learningassistantclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningTask {

    private Long id;
    private Long courseId;
    private Long teacherId;

    private Long chapterId;
    private Long sectionId;

    private String taskTitle;
    private String taskType;
    private String description;
    private String status;

    private String startTime;
    private String endTime;
    private String createTime;

    public LearningTask() {
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "任务ID：" + id
                + "，标题：" + safe(taskTitle)
                + "，类型：" + safe(taskType)
                + "，状态：" + safe(status);
    }

    private String safe(String value) {
        return value == null ? "暂无" : value;
    }
}