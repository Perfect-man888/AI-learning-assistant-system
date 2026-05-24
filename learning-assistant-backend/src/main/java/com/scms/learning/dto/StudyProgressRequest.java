package com.scms.learning.dto;


public class StudyProgressRequest {

    private Long courseId;
    private Long chapterId;
    private Long sectionId;
    private Long studentId;
    private String status;
    private Integer progressPercent;

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
}