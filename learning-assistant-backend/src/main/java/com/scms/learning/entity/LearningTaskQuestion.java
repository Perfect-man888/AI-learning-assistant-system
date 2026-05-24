package com.scms.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("learning_task_question")
public class LearningTaskQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_id")
    private Long taskId;

    @TableField("question_id")
    private Long questionId;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("create_time")
    private LocalDateTime createTime;

    public LearningTaskQuestion() {
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
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

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}