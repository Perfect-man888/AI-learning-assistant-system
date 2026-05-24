package com.scms.learning.vo;

import java.time.LocalDateTime;

public class AnswerRecordDetailVO {

    private Long id;
    private Long taskId;
    private Long questionId;
    private Long studentId;

    private String studentName;
    private String className;

    private String questionText;
    private String studentAnswer;
    private String correctAnswer;
    private String knowledgePoint;

    private Integer isCorrect;
    private Integer score;
    private LocalDateTime submitTime;

    public AnswerRecordDetailVO() {
    }

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getQuestionId() {
        return questionId;
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

    public String getQuestionText() {
        return questionText;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public Integer getIsCorrect() {
        return isCorrect;
    }

    public Integer getScore() {
        return score;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public void setIsCorrect(Integer isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }
}