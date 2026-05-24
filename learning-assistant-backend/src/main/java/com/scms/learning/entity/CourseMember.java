package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("course_member")
public class CourseMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private Long studentId;

    private LocalDateTime joinTime;

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }
}