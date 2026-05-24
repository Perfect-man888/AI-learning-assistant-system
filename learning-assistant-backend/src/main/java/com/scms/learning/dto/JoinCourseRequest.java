package com.scms.learning.dto;

import jakarta.validation.constraints.NotNull;

public class JoinCourseRequest {

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    public Long getCourseId() {
        return courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}