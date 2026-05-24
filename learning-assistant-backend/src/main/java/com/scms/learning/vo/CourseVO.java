package com.scms.learning.vo;

import java.time.LocalDateTime;

public class CourseVO {

    private Long id;

    private String courseName;

    private String description;

    private Long teacherId;

    private String teacherName;

    private String className;

    private LocalDateTime createTime;

    private Boolean joined;

    public Long getId() {
        return id;
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

    public String getTeacherName() {
        return teacherName;
    }

    public String getClassName() {
        return className;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Boolean getJoined() {
        return joined;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setJoined(Boolean joined) {
        this.joined = joined;
    }
}