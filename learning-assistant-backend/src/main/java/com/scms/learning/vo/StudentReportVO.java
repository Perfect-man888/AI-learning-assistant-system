package com.scms.learning.vo;

import java.util.ArrayList;
import java.util.List;

public class StudentReportVO {

    private Long studentId;
    private Long courseId;

    private Integer totalSections;
    private Integer completedSections;
    private Integer unfinishedSections;
    private Integer progressPercent;

    private Integer totalAnswers;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer accuracy;
    private Integer averageScore;

    private Integer wrongQuestionTotal;
    private Integer masteredWrongQuestions;
    private Integer unmasteredWrongQuestions;

    private Integer reviewTaskCount;

    private List<KnowledgePointStatVO> weakKnowledgePoints = new ArrayList<>();

    private String mainWeakPoint;
    private String learningSuggestion;

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

    public Integer getTotalSections() {
        return totalSections;
    }

    public void setTotalSections(Integer totalSections) {
        this.totalSections = totalSections;
    }

    public Integer getCompletedSections() {
        return completedSections;
    }

    public void setCompletedSections(Integer completedSections) {
        this.completedSections = completedSections;
    }

    public Integer getUnfinishedSections() {
        return unfinishedSections;
    }

    public void setUnfinishedSections(Integer unfinishedSections) {
        this.unfinishedSections = unfinishedSections;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Integer getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Integer totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(Integer wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getWrongQuestionTotal() {
        return wrongQuestionTotal;
    }

    public void setWrongQuestionTotal(Integer wrongQuestionTotal) {
        this.wrongQuestionTotal = wrongQuestionTotal;
    }

    public Integer getMasteredWrongQuestions() {
        return masteredWrongQuestions;
    }

    public void setMasteredWrongQuestions(Integer masteredWrongQuestions) {
        this.masteredWrongQuestions = masteredWrongQuestions;
    }

    public Integer getUnmasteredWrongQuestions() {
        return unmasteredWrongQuestions;
    }

    public void setUnmasteredWrongQuestions(Integer unmasteredWrongQuestions) {
        this.unmasteredWrongQuestions = unmasteredWrongQuestions;
    }

    public Integer getReviewTaskCount() {
        return reviewTaskCount;
    }

    public void setReviewTaskCount(Integer reviewTaskCount) {
        this.reviewTaskCount = reviewTaskCount;
    }

    public List<KnowledgePointStatVO> getWeakKnowledgePoints() {
        return weakKnowledgePoints;
    }

    public void setWeakKnowledgePoints(List<KnowledgePointStatVO> weakKnowledgePoints) {
        this.weakKnowledgePoints = weakKnowledgePoints;
    }

    public String getMainWeakPoint() {
        return mainWeakPoint;
    }

    public void setMainWeakPoint(String mainWeakPoint) {
        this.mainWeakPoint = mainWeakPoint;
    }

    public String getLearningSuggestion() {
        return learningSuggestion;
    }

    public void setLearningSuggestion(String learningSuggestion) {
        this.learningSuggestion = learningSuggestion;
    }
}