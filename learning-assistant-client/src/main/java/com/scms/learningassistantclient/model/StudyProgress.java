package com.scms.learningassistantclient.model;

public class StudyProgress {

    private Long id;
    private Long courseId;
    private Long chapterId;
    private Long sectionId;
    private Long studentId;

    private String chapterTitle;
    private String sectionTitle;

    private String status;
    private Integer progressPercent;
    private String updateTime;

    public StudyProgress() {
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

    public Long getStudentId() {
        return studentId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getStatus() {
        return status;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public String getUpdateTime() {
        return updateTime;
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

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isCompleted() {
        if (progressPercent != null && progressPercent >= 100) {
            return true;
        }

        if (status == null) {
            return false;
        }

        return "completed".equalsIgnoreCase(status)
                || "finish".equalsIgnoreCase(status)
                || "finished".equalsIgnoreCase(status)
                || "done".equalsIgnoreCase(status)
                || "已完成".equals(status)
                || "完成".equals(status);
    }

    public String getDisplayStatus() {
        if (isCompleted()) {
            return "已完成";
        }

        if (status == null || status.trim().isEmpty()) {
            return "未学习";
        }

        if ("studying".equalsIgnoreCase(status)
                || "learning".equalsIgnoreCase(status)
                || "学习中".equals(status)) {
            return "学习中";
        }

        if ("not_started".equalsIgnoreCase(status)
                || "notStarted".equalsIgnoreCase(status)
                || "未开始".equals(status)
                || "未学习".equals(status)) {
            return "未学习";
        }

        return status;
    }

    public String getDisplaySectionName() {
        if (sectionTitle != null && !sectionTitle.trim().isEmpty()) {
            return sectionTitle;
        }

        if (sectionId != null) {
            return "小节ID：" + sectionId;
        }

        return "未知小节";
    }
}