package com.scms.learning.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.dto.ChapterCreateRequest;
import com.scms.learning.entity.Chapter;
import com.scms.learning.mapper.ChapterMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {

    private final ChapterMapper chapterMapper;

    public ChapterService(ChapterMapper chapterMapper) {
        this.chapterMapper = chapterMapper;
    }

    public List<Chapter> getChaptersByCourseId(Long courseId) {
        LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chapter::getCourseId, courseId)
                .orderByAsc(Chapter::getSortOrder)
                .orderByAsc(Chapter::getId);

        return chapterMapper.selectList(wrapper);
    }

    public Chapter createChapter(Long courseId, ChapterCreateRequest request) {
        if (request.getChapterTitle() == null || request.getChapterTitle().trim().isEmpty()) {
            throw new RuntimeException("章节标题不能为空");
        }

        Chapter chapter = new Chapter();
        chapter.setCourseId(courseId);
        chapter.setChapterTitle(request.getChapterTitle().trim());

        if (request.getSortOrder() == null) {
            chapter.setSortOrder(1);
        } else {
            chapter.setSortOrder(request.getSortOrder());
        }

        chapterMapper.insert(chapter);

        return chapter;
    }
}
