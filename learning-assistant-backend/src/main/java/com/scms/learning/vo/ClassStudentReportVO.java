package com.scms.learning.vo;

public class ClassStudentReportVO {

    private Long studentId;
    private String studentName;
    private String className;
    private Long courseId;

    private Integer totalSections;
    private Integer completedSections;
    private Integer studyProgressPercent;

    private Integer answerCount;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer quizAccuracy;
    private Integer averageScore;

    private Integer wrongQuestionCount;

    public ClassStudentReportVO() {
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

    public Integer getStudyProgressPercent() {
        return studyProgressPercent;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public Integer getQuizAccuracy() {
        return quizAccuracy;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public Integer getWrongQuestionCount() {
        return wrongQuestionCount;
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

    public void setStudyProgressPercent(Integer studyProgressPercent) {
        this.studyProgressPercent = studyProgressPercent;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public void setQuizAccuracy(Integer quizAccuracy) {
        this.quizAccuracy = quizAccuracy;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public void setWrongQuestionCount(Integer wrongQuestionCount) {
        this.wrongQuestionCount = wrongQuestionCount;
    }
}