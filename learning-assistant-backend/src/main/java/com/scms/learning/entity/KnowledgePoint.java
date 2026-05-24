package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("knowledge_point")
public class KnowledgePoint {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("section_id")
    private Long sectionId;

    @TableField("point_name")
    private String pointName;

    private String description;

    private String difficulty;

    @TableField("is_key_point")
    private Integer isKeyPoint;

    @TableField("is_easy_wrong")
    private Integer isEasyWrong;

    @TableField("create_time")
    private LocalDateTime createTime;

    public KnowledgePoint() {
    }

    public Long getId() {
        return id;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public String getPointName() {
        return pointName;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Integer getIsKeyPoint() {
        return isKeyPoint;
    }

    public Integer getIsEasyWrong() {
        return isEasyWrong;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setIsKeyPoint(Integer isKeyPoint) {
        this.isKeyPoint = isKeyPoint;
    }

    public void setIsEasyWrong(Integer isEasyWrong) {
        this.isEasyWrong = isEasyWrong;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}