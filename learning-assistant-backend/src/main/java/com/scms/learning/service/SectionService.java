package com.scms.learning.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.dto.SectionCreateRequest;
import com.scms.learning.entity.Section;
import com.scms.learning.mapper.SectionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    private final SectionMapper sectionMapper;

    public SectionService(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }

    public List<Section> getSectionsByChapterId(Long chapterId) {
        LambdaQueryWrapper<Section> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Section::getChapterId, chapterId)
                .orderByAsc(Section::getSortOrder)
                .orderByAsc(Section::getId);

        return sectionMapper.selectList(wrapper);
    }

    public Section createSection(Long chapterId, SectionCreateRequest request) {
        if (request.getSectionTitle() == null || request.getSectionTitle().trim().isEmpty()) {
            throw new RuntimeException("小节标题不能为空");
        }

        Section section = new Section();
        section.setChapterId(chapterId);
        section.setSectionTitle(request.getSectionTitle().trim());
        section.setContent(request.getContent());
        section.setKnowledgePoints(request.getKnowledgePoints());

        if (request.getSortOrder() == null) {
            section.setSortOrder(1);
        } else {
            section.setSortOrder(request.getSortOrder());
        }

        sectionMapper.insert(section);

        return section;
    }
}