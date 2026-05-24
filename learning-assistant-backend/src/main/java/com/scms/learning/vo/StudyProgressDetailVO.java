package com.scms.learning.vo;

import java.time.LocalDateTime;

public class StudyProgressDetailVO {

    private Long id;
    private Long courseId;
    private Long chapterId;
    private Long sectionId;
    private Long studentId;

    private String chapterTitle;
    private String sectionTitle;

    private String status;
    private Integer progressPercent;
    private LocalDateTime updateTime;

    public StudyProgressDetailVO() {
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

    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getSectionTitle() {
        return sectionTitle;
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

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
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