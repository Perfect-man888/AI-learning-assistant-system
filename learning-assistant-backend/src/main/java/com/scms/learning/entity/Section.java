package com.scms.learning.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("section")
public class Section {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("chapter_id")
    private Long chapterId;

    @TableField("section_title")
    private String sectionTitle;

    @TableField("content")
    private String content;

    @TableField("knowledge_points")
    private String knowledgePoints;

    @TableField("sort_order")
    private Integer sortOrder;

    public Section() {
    }

    public Long getId() {
        return id;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getContent() {
        return content;
    }

    public String getKnowledgePoints() {
        return knowledgePoints;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setKnowledgePoints(String knowledgePoints) {
        this.knowledgePoints = knowledgePoints;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
