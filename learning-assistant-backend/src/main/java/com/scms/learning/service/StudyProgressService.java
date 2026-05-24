package com.scms.learning.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.dto.StudyProgressRequest;
import com.scms.learning.entity.StudyProgress;
import com.scms.learning.mapper.StudyProgressMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.scms.learning.vo.StudentProgressSummaryVO;
import com.scms.learning.vo.StudyProgressDetailVO;

@Service
public class StudyProgressService {

    private final StudyProgressMapper studyProgressMapper;

    public StudyProgressService(StudyProgressMapper studyProgressMapper) {
        this.studyProgressMapper = studyProgressMapper;
    }

    public StudyProgress completeSection(StudyProgressRequest request) {
        if (request.getCourseId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        if (request.getStudentId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }

        if (request.getChapterId() == null) {
            throw new RuntimeException("章节ID不能为空");
        }

        if (request.getSectionId() == null) {
            throw new RuntimeException("小节ID不能为空");
        }

        LambdaQueryWrapper<StudyProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyProgress::getCourseId, request.getCourseId())
                .eq(StudyProgress::getStudentId, request.getStudentId())
                .eq(StudyProgress::getChapterId, request.getChapterId())
                .eq(StudyProgress::getSectionId, request.getSectionId());

        StudyProgress existing = studyProgressMapper.selectOne(wrapper);

        if (existing == null) {
            StudyProgress progress = new StudyProgress();
            progress.setCourseId(request.getCourseId());
            progress.setStudentId(request.getStudentId());
            progress.setChapterId(request.getChapterId());
            progress.setSectionId(request.getSectionId());
            progress.setStatus(request.getStatus() == null ? "completed" : request.getStatus());
            progress.setProgressPercent(request.getProgressPercent() == null ? 100 : request.getProgressPercent());
            progress.setUpdateTime(LocalDateTime.now());

            studyProgressMapper.insert(progress);
            return progress;
        }

        existing.setStatus(request.getStatus() == null ? "completed" : request.getStatus());
        existing.setProgressPercent(request.getProgressPercent() == null ? 100 : request.getProgressPercent());
        existing.setUpdateTime(LocalDateTime.now());

        studyProgressMapper.updateById(existing);
        return existing;
    }

    public List<StudentProgressSummaryVO> getCourseProgressSummary(Long courseId) {
        if (courseId == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        List<StudentProgressSummaryVO> list = studyProgressMapper.selectCourseProgressSummary(courseId);

        for (StudentProgressSummaryVO item : list) {
            int total = item.getTotalSections() == null ? 0 : item.getTotalSections();
            int completed = item.getCompletedSections() == null ? 0 : item.getCompletedSections();

            int percent;
            if (total == 0) {
                percent = 0;
            } else {
                percent = completed * 100 / total;
            }

            item.setProgressPercent(percent);
        }

        return list;
    }

    public List<StudyProgressDetailVO> getStudentCourseProgress(Long studentId, Long courseId) {
        if (studentId == null) {
            throw new RuntimeException("学生ID不能为空");
        }

        if (courseId == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        return studyProgressMapper.selectStudentCourseProgressDetail(studentId, courseId);
    }
}