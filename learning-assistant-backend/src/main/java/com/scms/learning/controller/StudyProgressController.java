package com.scms.learning.controller;


import com.scms.learning.dto.StudyProgressRequest;
import com.scms.learning.entity.StudyProgress;
import com.scms.learning.service.StudyProgressService;
import org.springframework.web.bind.annotation.*;
import com.scms.learning.vo.StudentProgressSummaryVO;
import com.scms.learning.vo.StudyProgressDetailVO;

import java.util.List;

@RestController
@RequestMapping("/api/study-progress")
@CrossOrigin
public class StudyProgressController {

    private final StudyProgressService studyProgressService;

    public StudyProgressController(StudyProgressService studyProgressService) {
        this.studyProgressService = studyProgressService;
    }

    @PostMapping("/section")
    public StudyProgress completeSection(@RequestBody StudyProgressRequest request) {
        return studyProgressService.completeSection(request);
    }

    @GetMapping("/course/{courseId}/summary")
    public List<StudentProgressSummaryVO> getCourseProgressSummary(@PathVariable Long courseId) {
        return studyProgressService.getCourseProgressSummary(courseId);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public List<StudyProgressDetailVO> getStudentCourseProgress(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return studyProgressService.getStudentCourseProgress(studentId, courseId);
    }
}