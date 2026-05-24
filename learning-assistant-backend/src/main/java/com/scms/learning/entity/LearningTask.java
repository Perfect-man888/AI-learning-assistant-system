package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("learning_task")
public class LearningTask {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("chapter_id")
    private Long chapterId;

    @TableField("section_id")
    private Long sectionId;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("task_title")
    private String taskTitle;

    private String description;

    private String status;

    @TableField("task_type")
    private String taskType;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    public LearningTask() {
    }

    public Long getId() {
        return id;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskType() {
        return taskType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getCreateTime() {
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

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}