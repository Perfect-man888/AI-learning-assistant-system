package com.scms.learningassistantclient.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassLearningReport {

    private Long courseId;

    private Integer studentCount;
    private Integer averageStudyProgress;

    private Integer totalAnswerCount;
    private Integer totalCorrectCount;
    private Integer totalWrongCount;
    private Integer averageQuizAccuracy;
    private Integer averageScore;

    private Map<String, Integer> weakKnowledgePoints = new LinkedHashMap<>();
    private List<ClassStudentReport> students = new ArrayList<>();

    private String teachingSuggestion;

    public ClassLearningReport() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public Integer getAverageStudyProgress() {
        return averageStudyProgress;
    }

    public Integer getTotalAnswerCount() {
        return totalAnswerCount;
    }

    public Integer getTotalCorrectCount() {
        return totalCorrectCount;
    }

    public Integer getTotalWrongCount() {
        return totalWrongCount;
    }

    public Integer getAverageQuizAccuracy() {
        return averageQuizAccuracy;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public Map<String, Integer> getWeakKnowledgePoints() {
        return weakKnowledgePoints;
    }

    public List<ClassStudentReport> getStudents() {
        return students;
    }

    public String getTeachingSuggestion() {
        return teachingSuggestion;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public void setAverageStudyProgress(Integer averageStudyProgress) {
        this.averageStudyProgress = averageStudyProgress;
    }

    public void setTotalAnswerCount(Integer totalAnswerCount) {
        this.totalAnswerCount = totalAnswerCount;
    }

    public void setTotalCorrectCount(Integer totalCorrectCount) {
        this.totalCorrectCount = totalCorrectCount;
    }

    public void setTotalWrongCount(Integer totalWrongCount) {
        this.totalWrongCount = totalWrongCount;
    }

    public void setAverageQuizAccuracy(Integer averageQuizAccuracy) {
        this.averageQuizAccuracy = averageQuizAccuracy;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public void setWeakKnowledgePoints(Map<String, Integer> weakKnowledgePoints) {
        this.weakKnowledgePoints = weakKnowledgePoints;
    }

    public void setStudents(List<ClassStudentReport> students) {
        this.students = students;
    }

    public void setTeachingSuggestion(String teachingSuggestion) {
        this.teachingSuggestion = teachingSuggestion;
    }
}