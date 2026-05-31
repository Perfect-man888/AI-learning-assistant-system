package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.AnswerRecord;
import com.scms.learning.entity.LearningTask;
import com.scms.learning.entity.StudyProgress;
import com.scms.learning.entity.WrongQuestion;
import com.scms.learning.mapper.AnswerRecordMapper;
import com.scms.learning.mapper.LearningTaskMapper;
import com.scms.learning.mapper.StudyProgressMapper;
import com.scms.learning.mapper.WrongQuestionMapper;
import com.scms.learning.vo.KnowledgePointStatVO;
import com.scms.learning.vo.StudentReportVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class ReportController {

    private final StudyProgressMapper studyProgressMapper;
    private final AnswerRecordMapper answerRecordMapper;
    private final WrongQuestionMapper wrongQuestionMapper;
    private final LearningTaskMapper learningTaskMapper;

    public ReportController(
            StudyProgressMapper studyProgressMapper,
            AnswerRecordMapper answerRecordMapper,
            WrongQuestionMapper wrongQuestionMapper,
            LearningTaskMapper learningTaskMapper
    ) {
        this.studyProgressMapper = studyProgressMapper;
        this.answerRecordMapper = answerRecordMapper;
        this.wrongQuestionMapper = wrongQuestionMapper;
        this.learningTaskMapper = learningTaskMapper;
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public StudentReportVO getStudentCourseReport(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        if (studentId == null) {
            throw new RuntimeException("学生ID不能为空");
        }

        if (courseId == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        StudentReportVO report = new StudentReportVO();

        report.setStudentId(studentId);
        report.setCourseId(courseId);

        fillProgress(report, studentId, courseId);
        fillAnswerStats(report, studentId, courseId);
        fillWrongQuestionStats(report, studentId, courseId);
        fillReviewTaskStats(report, courseId);
        fillSuggestion(report);

        return report;
    }

    private void fillProgress(StudentReportVO report, Long studentId, Long courseId) {
        LambdaQueryWrapper<StudyProgress> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StudyProgress::getStudentId, studentId)
                .eq(StudyProgress::getCourseId, courseId);

        List<StudyProgress> progressList = studyProgressMapper.selectList(wrapper);

        int total = progressList == null ? 0 : progressList.size();
        int completed = 0;

        if (progressList != null) {
            for (StudyProgress progress : progressList) {
                if (progress == null) {
                    continue;
                }

                String status = progress.getStatus();

                if ("completed".equalsIgnoreCase(status)) {
                    completed++;
                }
            }
        }

        int unfinished = total - completed;
        int percent = total == 0 ? 0 : completed * 100 / total;

        report.setTotalSections(total);
        report.setCompletedSections(completed);
        report.setUnfinishedSections(unfinished);
        report.setProgressPercent(percent);
    }

    private void fillAnswerStats(StudentReportVO report, Long studentId, Long courseId) {
        LambdaQueryWrapper<AnswerRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(AnswerRecord::getStudentId, studentId)
                .eq(AnswerRecord::getCourseId, courseId);

        List<AnswerRecord> records = answerRecordMapper.selectList(wrapper);

        int total = records == null ? 0 : records.size();
        int correct = 0;
        int totalScore = 0;

        if (records != null) {
            for (AnswerRecord record : records) {
                if (record == null) {
                    continue;
                }

                if (record.getIsCorrect() != null && record.getIsCorrect() == 1) {
                    correct++;
                }

                if (record.getScore() != null) {
                    totalScore += record.getScore();
                }
            }
        }

        int wrong = total - correct;
        int accuracy = total == 0 ? 0 : correct * 100 / total;
        int averageScore = total == 0 ? 0 : totalScore / total;

        report.setTotalAnswers(total);
        report.setCorrectAnswers(correct);
        report.setWrongAnswers(wrong);
        report.setAccuracy(accuracy);
        report.setAverageScore(averageScore);
    }

    private void fillWrongQuestionStats(StudentReportVO report, Long studentId, Long courseId) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(WrongQuestion::getStudentId, studentId)
                .eq(WrongQuestion::getCourseId, courseId)
                .orderByDesc(WrongQuestion::getWrongCount)
                .orderByDesc(WrongQuestion::getLastWrongTime);

        List<WrongQuestion> wrongQuestions = wrongQuestionMapper.selectList(wrapper);

        int total = wrongQuestions == null ? 0 : wrongQuestions.size();
        int mastered = 0;
        int unmastered = 0;

        Map<String, Integer> weakMap = new LinkedHashMap<>();

        if (wrongQuestions != null) {
            for (WrongQuestion wrongQuestion : wrongQuestions) {
                if (wrongQuestion == null) {
                    continue;
                }

                Integer isMastered = wrongQuestion.getIsMastered();

                if (isMastered != null && isMastered == 1) {
                    mastered++;
                } else {
                    unmastered++;
                }

                String point = wrongQuestion.getKnowledgePointName();

                if (point == null || point.trim().isEmpty()) {
                    point = "未标注知识点";
                }

                Integer wrongCount = wrongQuestion.getWrongCount();

                if (wrongCount == null || wrongCount <= 0) {
                    wrongCount = 1;
                }

                weakMap.put(point, weakMap.getOrDefault(point, 0) + wrongCount);
            }
        }

        List<KnowledgePointStatVO> weakList = new ArrayList<>();

        String mainWeakPoint = "暂无";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : weakMap.entrySet()) {
            KnowledgePointStatVO vo = new KnowledgePointStatVO(
                    entry.getKey(),
                    entry.getValue()
            );

            weakList.add(vo);

            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mainWeakPoint = entry.getKey();
            }
        }

        report.setWrongQuestionTotal(total);
        report.setMasteredWrongQuestions(mastered);
        report.setUnmasteredWrongQuestions(unmastered);
        report.setWeakKnowledgePoints(weakList);
        report.setMainWeakPoint(mainWeakPoint);
    }

    private void fillReviewTaskStats(StudentReportVO report, Long courseId) {
        LambdaQueryWrapper<LearningTask> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(LearningTask::getCourseId, courseId)
                .eq(LearningTask::getTaskType, "review");

        Long count = learningTaskMapper.selectCount(wrapper);

        report.setReviewTaskCount(count == null ? 0 : count.intValue());
    }

    private void fillSuggestion(StudentReportVO report) {
        StringBuilder builder = new StringBuilder();

        Integer totalSections = report.getTotalSections() == null ? 0 : report.getTotalSections();
        Integer unfinishedSections = report.getUnfinishedSections() == null ? 0 : report.getUnfinishedSections();

        if (totalSections == 0) {
            builder.append("1. 当前课程暂无学习进度记录，建议先完成课程小节学习。\n");
        } else if (unfinishedSections > 0) {
            builder.append("1. 你还有 ")
                    .append(unfinishedSections)
                    .append(" 个小节未完成，建议先补齐课程学习进度。\n");
        } else {
            builder.append("1. 课程小节已完成，可以进入测验巩固与错题复习阶段。\n");
        }

        Integer totalAnswers = report.getTotalAnswers() == null ? 0 : report.getTotalAnswers();
        Integer accuracy = report.getAccuracy() == null ? 0 : report.getAccuracy();

        if (totalAnswers == 0) {
            builder.append("2. 当前暂无测验记录，建议完成课程测验以检测掌握情况。\n");
        } else if (accuracy >= 85) {
            builder.append("2. 当前测验正确率较高，可以继续挑战更高难度题目。\n");
        } else if (accuracy >= 60) {
            builder.append("2. 当前测验表现中等，建议针对错题知识点继续练习。\n");
        } else {
            builder.append("2. 当前测验正确率偏低，建议重新学习相关小节后再进行测验。\n");
        }

        Integer unmastered = report.getUnmasteredWrongQuestions() == null ? 0 : report.getUnmasteredWrongQuestions();

        if (unmastered > 0) {
            builder.append("3. 当前仍有 ")
                    .append(unmastered)
                    .append(" 条未掌握错题，建议点击【个性化复习】生成 AI 相似题进行巩固。\n");

            builder.append("4. 当前重点薄弱知识点为【")
                    .append(report.getMainWeakPoint())
                    .append("】，建议优先复习相关概念、例题和错题。");
        } else {
            builder.append("3. 当前错题已基本掌握，可以保持复习频率，定期完成综合测验。");
        }

        report.setLearningSuggestion(builder.toString());
    }
}