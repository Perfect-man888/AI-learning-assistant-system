package com.scms.learning.dto;

public class ChapterCreateRequest {

    private String chapterTitle;
    private Integer sortOrder;

    public String getChapterTitle() {
        return chapterTitle;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}