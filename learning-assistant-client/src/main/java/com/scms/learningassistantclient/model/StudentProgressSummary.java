package com.scms.learningassistantclient.model;

import java.util.ArrayList;
import java.util.List;

public class StudentProgressSummary {

    private Long studentId;
    private String studentName;
    private String className;
    private Long courseId;
    private Integer totalSections;
    private Integer completedSections;
    private Integer progressPercent;

    private List<StudyProgress> progressList = new ArrayList<>();

    public StudentProgressSummary() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getClassName() {
        return className;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Integer getTotalSections() {
        return totalSections;
    }

    public Integer getCompletedSections() {
        return completedSections;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public List<StudyProgress> getProgressList() {
        return progressList;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setTotalSections(Integer totalSections) {
        this.totalSections = totalSections;
    }

    public void setCompletedSections(Integer completedSections) {
        this.completedSections = completedSections;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public void setProgressList(List<StudyProgress> progressList) {
        if (progressList == null) {
            this.progressList = new ArrayList<>();
        } else {
            this.progressList = progressList;
        }
    }
}