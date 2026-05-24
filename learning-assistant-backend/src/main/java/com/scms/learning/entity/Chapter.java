package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

@TableName("chapter")
public class Chapter {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("chapter_title")
    private String chapterTitle;

    @TableField("sort_order")
    private Integer sortOrder;

    public Chapter() {
    }

    public Chapter(Long id, Long courseId, String chapterTitle, Integer sortOrder) {
        this.id = id;
        this.courseId = courseId;
        this.chapterTitle = chapterTitle;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}