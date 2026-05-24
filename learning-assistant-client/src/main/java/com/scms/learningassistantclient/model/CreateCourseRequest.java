package com.scms.learningassistantclient.model;

public class CreateCourseRequest {

    private String courseName;
    private String description;
    private Long teacherId;

    public CreateCourseRequest() {
    }

    public CreateCourseRequest(String courseName, String description, Long teacherId) {
        this.courseName = courseName;
        this.description = description;
        this.teacherId = teacherId;
    }

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