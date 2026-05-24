package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("wrong_question")
public class WrongQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("student_id")
    private Long studentId;

    @TableField("question_id")
    private Long questionId;

    @TableField("course_id")
    private Long courseId;

    @TableField("chapter_id")
    private Long chapterId;

    @TableField("section_id")
    private Long sectionId;

    @TableField("knowledge_point_id")
    private Long knowledgePointId;

    @TableField("knowledge_point_name")
    private String knowledgePointName;

    @TableField("wrong_count")
    private Integer wrongCount;

    @TableField("last_wrong_time")
    private LocalDateTime lastWrongTime;

    @TableField("is_mastered")
    private Integer isMastered;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public WrongQuestion() {
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getQuestionId() {
        return questionId;
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

    public String getKnowledgePointName() {
        return knowledgePointName;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public LocalDateTime getLastWrongTime() {
        return lastWrongTime;
    }

    public Integer getIsMastered() {
        return isMastered;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public void setKnowledgePointName(String knowledgePointName) {
        this.knowledgePointName = knowledgePointName;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public void setLastWrongTime(LocalDateTime lastWrongTime) {
        this.lastWrongTime = lastWrongTime;
    }

    public void setIsMastered(Integer isMastered) {
        this.isMastered = isMastered;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}