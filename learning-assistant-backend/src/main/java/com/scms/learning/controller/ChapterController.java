package com.scms.learning.controller;


import com.scms.learning.dto.ChapterCreateRequest;
import com.scms.learning.entity.Chapter;
import com.scms.learning.service.ChapterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/courses/{courseId}/chapters")
    public List<Chapter> getChaptersByCourseId(@PathVariable Long courseId) {
        return chapterService.getChaptersByCourseId(courseId);
    }

    @PostMapping("/courses/{courseId}/chapters")
    public Chapter createChapter(
            @PathVariable Long courseId,
            @RequestBody ChapterCreateRequest request
    ) {
        return chapterService.createChapter(courseId, request);
    }
}