package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("question")
public class Question {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("chapter_id")
    private Long chapterId;

    @TableField("section_id")
    private Long sectionId;

    @TableField("question_type")
    private String questionType;

    @TableField("question_text")
    private String questionText;

    private String options;

    private String answer;

    private String analysis;

    @TableField("knowledge_point")
    private String knowledgePoint;

    @TableField("knowledge_point_id")
    private Long knowledgePointId;

    private String difficulty;

    @TableField("create_time")
    private LocalDateTime createTime;



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

    public LocalDateTime getCreateTime() {
        return createTime;
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

    public void setKnowledgePointId(Long knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}