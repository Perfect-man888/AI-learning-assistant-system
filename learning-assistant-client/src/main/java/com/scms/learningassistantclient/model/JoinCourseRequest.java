package com.scms.learningassistantclient.model;

public class JoinCourseRequest {

    private Long courseId;
    private Long studentId;

    public JoinCourseRequest() {
    }

    public JoinCourseRequest(Long courseId, Long studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

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