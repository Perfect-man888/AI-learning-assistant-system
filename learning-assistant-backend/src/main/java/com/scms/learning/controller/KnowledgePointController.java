package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.KnowledgePoint;
import com.scms.learning.mapper.KnowledgePointMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge-points")
@CrossOrigin
public class KnowledgePointController {

    private final KnowledgePointMapper knowledgePointMapper;

    public KnowledgePointController(KnowledgePointMapper knowledgePointMapper) {
        this.knowledgePointMapper = knowledgePointMapper;
    }

    @PostMapping
    public KnowledgePoint createKnowledgePoint(@RequestBody KnowledgePoint knowledgePoint) {
        if (knowledgePoint.getSectionId() == null) {
            throw new RuntimeException("小节ID不能为空");
        }

        if (knowledgePoint.getPointName() == null || knowledgePoint.getPointName().trim().isEmpty()) {
            throw new RuntimeException("知识点名称不能为空");
        }

        if (knowledgePoint.getDifficulty() == null || knowledgePoint.getDifficulty().trim().isEmpty()) {
            knowledgePoint.setDifficulty("normal");
        }

        if (knowledgePoint.getIsKeyPoint() == null) {
            knowledgePoint.setIsKeyPoint(0);
        }

        if (knowledgePoint.getIsEasyWrong() == null) {
            knowledgePoint.setIsEasyWrong(0);
        }

        knowledgePoint.setCreateTime(LocalDateTime.now());

        knowledgePointMapper.insert(knowledgePoint);
        return knowledgePoint;
    }

    @GetMapping("/section/{sectionId}")
    public List<KnowledgePoint> getBySectionId(@PathVariable Long sectionId) {
        LambdaQueryWrapper<KnowledgePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgePoint::getSectionId, sectionId)
                .orderByAsc(KnowledgePoint::getId);

        return knowledgePointMapper.selectList(wrapper);
    }

    @DeleteMapping("/{id}")
    public String deleteKnowledgePoint(@PathVariable Long id) {
        knowledgePointMapper.deleteById(id);
        return "删除成功";
    }
}