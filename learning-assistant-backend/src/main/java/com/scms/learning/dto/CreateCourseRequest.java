package com.scms.learning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCourseRequest {

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    private String description;

    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}