package com.scms.learningassistantclient.model;

public class CourseChapter {

    private Long id;
    private Long courseId;
    private String chapterTitle;
    private Integer sortOrder;

    public CourseChapter() {
    }

    public CourseChapter(Long id, Long courseId, String chapterTitle, Integer sortOrder) {
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

    @Override
    public String toString() {
        return chapterTitle;
    }
}