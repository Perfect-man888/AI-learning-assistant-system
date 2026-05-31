package com.scms.learning.dto;

public class ReviewTaskGenerateRequest {

    private Long studentId;
    private Long courseId;
    private Integer count;

    public ReviewTaskGenerateRequest() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}