package com.scms.learningassistantclient.model;

public class Course {

    private Long id;
    private String courseName;
    private String description;
    private Long teacherId;
    private String teacherName;
    private String className;
    private String createTime;
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

    public String getCreateTime() {
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setJoined(Boolean joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return "课程ID：" + id
                + "\n课程名称：" + nullToEmpty(courseName)
                + "\n教师：" + nullToEmpty(teacherName)
                + "\n班级：" + nullToEmpty(className)
                + "\n简介：" + nullToEmpty(description)
                + "\n状态：" + (Boolean.TRUE.equals(joined) ? "已加入" : "未加入");
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}