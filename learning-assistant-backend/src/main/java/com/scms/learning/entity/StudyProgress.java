package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("study_progress")
public class StudyProgress {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("chapter_id")
    private Long chapterId;

    @TableField("section_id")
    private Long sectionId;

    @TableField("student_id")
    private Long studentId;

    @TableField("status")
    private String status;

    @TableField("progress_percent")
    private Integer progressPercent;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public StudyProgress() {
    }

    public Long getId() {
        return id;
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

    public Long getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}