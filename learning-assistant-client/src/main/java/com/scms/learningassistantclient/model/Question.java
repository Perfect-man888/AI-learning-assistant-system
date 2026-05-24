package com.scms.learningassistantclient.model;

public class Question {

    private Long id;
    private Long courseId;
    private Long chapterId;
    private Long sectionId;

    private String questionType;
    private String questionText;
    private String options;
    private String answer;
    private String analysis;
    private String knowledgePoint;
    private String difficulty;
    private String createTime;

    private Long knowledgePointId;

    public Question() {
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

    public String getQuestionType() {
        return questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public Long getKnowledgePointId() {
        return knowledgePointId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setKnowledgePointId(Long knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}