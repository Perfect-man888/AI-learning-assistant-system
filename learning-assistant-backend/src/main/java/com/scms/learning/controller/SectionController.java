package com.scms.learning.controller;


import com.scms.learning.dto.SectionCreateRequest;
import com.scms.learning.entity.Section;
import com.scms.learning.service.SectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/chapters/{chapterId}/sections")
    public List<Section> getSectionsByChapterId(@PathVariable Long chapterId) {
        return sectionService.getSectionsByChapterId(chapterId);
    }

    @PostMapping("/chapters/{chapterId}/sections")
    public Section createSection(
            @PathVariable Long chapterId,
            @RequestBody SectionCreateRequest request
    ) {
        return sectionService.createSection(chapterId, request);
    }
}