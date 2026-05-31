package com.scms.learning.vo;

public class KnowledgePointStatVO {

    private String knowledgePoint;
    private Integer wrongCount;

    public KnowledgePointStatVO() {
    }

    public KnowledgePointStatVO(String knowledgePoint, Integer wrongCount) {
        this.knowledgePoint = knowledgePoint;
        this.wrongCount = wrongCount;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }
}