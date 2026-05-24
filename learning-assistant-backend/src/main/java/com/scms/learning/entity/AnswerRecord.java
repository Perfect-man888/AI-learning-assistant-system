package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("answer_record")
public class AnswerRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_id")
    private Long taskId;

    @TableField("question_id")
    private Long questionId;

    @TableField("student_id")
    private Long studentId;



    @TableField("student_answer")
    private String studentAnswer;
    @TableField("course_id")
    private Long courseId;

    @TableField("chapter_id")
    private Long chapterId;

    @TableField("section_id")
    private Long sectionId;

    @TableField("knowledge_point_id")
    private Long knowledgePointId;

    @TableField("answer_duration")
    private Integer answerDuration;

    @TableField("is_correct")
    private Integer isCorrect;

    private Integer score;

    @TableField("submit_time")
    private LocalDateTime submitTime;



    public AnswerRecord() {
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

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public Integer getIsCorrect() {
        return isCorrect;
    }

    public Integer getScore() {
        return score;
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

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public void setIsCorrect(Integer isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }
}