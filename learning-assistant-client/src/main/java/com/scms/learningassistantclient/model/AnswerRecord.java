package com.scms.learningassistantclient.model;

public class AnswerRecord {

    private Long id;
    private Long taskId;
    private Long questionId;
    private Long studentId;

    private String studentAnswer;
    private Integer isCorrect;
    private Integer score;
    private String submitTime;

    private Long courseId;
    private Long chapterId;
    private Long sectionId;
    private Long knowledgePointId;
    private Integer answerDuration;

    public AnswerRecord() {
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getKnowledgePointId() {
        return knowledgePointId;
    }

    public Integer getAnswerDuration() {
        return answerDuration;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public void setKnowledgePointId(Long knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public void setAnswerDuration(Integer answerDuration) {
        this.answerDuration = answerDuration;
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

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public Integer getIsCorrect() {
        return isCorrect;
    }

    public Integer getScore() {
        return score;
    }

    public String getSubmitTime() {
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

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public void setIsCorrect(Integer isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public boolean answerCorrect() {
        return isCorrect != null && isCorrect == 1;
    }
}