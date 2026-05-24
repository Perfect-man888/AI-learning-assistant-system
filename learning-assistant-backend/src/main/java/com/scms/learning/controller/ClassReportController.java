package com.scms.learning.controller;

import com.scms.learning.mapper.AnswerRecordMapper;
import com.scms.learning.mapper.StudyProgressMapper;
import com.scms.learning.vo.AnswerRecordDetailVO;
import com.scms.learning.vo.ClassLearningReportVO;
import com.scms.learning.vo.ClassStudentReportVO;
import com.scms.learning.vo.StudentProgressSummaryVO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/class-report")
@CrossOrigin
public class ClassReportController {

    private final StudyProgressMapper studyProgressMapper;
    private final AnswerRecordMapper answerRecordMapper;

    public ClassReportController(
            StudyProgressMapper studyProgressMapper,
            AnswerRecordMapper answerRecordMapper
    ) {
        this.studyProgressMapper = studyProgressMapper;
        this.answerRecordMapper = answerRecordMapper;
    }

    @GetMapping("/course/{courseId}")
    public ClassLearningReportVO getClassReport(@PathVariable Long courseId) {
        if (courseId == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        List<StudentProgressSummaryVO> progressSummaries =
                studyProgressMapper.selectCourseProgressSummary(courseId);

        ClassLearningReportVO report = new ClassLearningReportVO();
        report.setCourseId(courseId);

        if (progressSummaries == null || progressSummaries.isEmpty()) {
            report.setStudentCount(0);
            report.setAverageStudyProgress(0);
            report.setTotalAnswerCount(0);
            report.setTotalCorrectCount(0);
            report.setTotalWrongCount(0);
            report.setAverageQuizAccuracy(0);
            report.setAverageScore(0);
            report.setTeachingSuggestion("当前课程暂无学生加入，暂不能生成班级综合学习报告。");
            return report;
        }

        List<ClassStudentReportVO> studentReports = new ArrayList<>();
        Map<String, Integer> weakPointMap = new HashMap<>();

        int studentCount = progressSummaries.size();
        int totalStudyProgress = 0;

        int totalAnswerCount = 0;
        int totalCorrectCount = 0;
        int totalWrongCount = 0;
        int totalScore = 0;

        for (StudentProgressSummaryVO summary : progressSummaries) {
            Long studentId = summary.getStudentId();

            int totalSections = safeInt(summary.getTotalSections());
            int completedSections = safeInt(summary.getCompletedSections());
            int studyProgressPercent = totalSections == 0 ? 0 : completedSections * 100 / totalSections;

            List<AnswerRecordDetailVO> answerRecords =
                    answerRecordMapper.selectAnswerRecordDetailsByStudentIdAndCourseId(studentId, courseId);

            List<AnswerRecordDetailVO> wrongRecords =
                    answerRecordMapper.selectWrongAnswerDetailsByStudentIdAndCourseId(studentId, courseId);

            int answerCount = answerRecords == null ? 0 : answerRecords.size();
            int correctCount = 0;
            int scoreSum = 0;

            if (answerRecords != null) {
                for (AnswerRecordDetailVO record : answerRecords) {
                    if (record.getIsCorrect() != null && record.getIsCorrect() == 1) {
                        correctCount++;
                    }

                    if (record.getScore() != null) {
                        scoreSum += record.getScore();
                    }
                }
            }

            int wrongCount = answerCount - correctCount;
            int quizAccuracy = answerCount == 0 ? 0 : correctCount * 100 / answerCount;
            int averageScore = answerCount == 0 ? 0 : scoreSum / answerCount;
            int wrongQuestionCount = wrongRecords == null ? 0 : wrongRecords.size();

            if (wrongRecords != null) {
                for (AnswerRecordDetailVO wrong : wrongRecords) {
                    String point = wrong.getKnowledgePoint();

                    if (point == null || point.trim().isEmpty()) {
                        point = "未标注知识点";
                    }

                    weakPointMap.put(point, weakPointMap.getOrDefault(point, 0) + 1);
                }
            }

            ClassStudentReportVO item = new ClassStudentReportVO();
            item.setStudentId(studentId);
            item.setStudentName(summary.getStudentName());
            item.setClassName(summary.getClassName());
            item.setCourseId(courseId);

            item.setTotalSections(totalSections);
            item.setCompletedSections(completedSections);
            item.setStudyProgressPercent(studyProgressPercent);

            item.setAnswerCount(answerCount);
            item.setCorrectCount(correctCount);
            item.setWrongCount(wrongCount);
            item.setQuizAccuracy(quizAccuracy);
            item.setAverageScore(averageScore);
            item.setWrongQuestionCount(wrongQuestionCount);

            studentReports.add(item);

            totalStudyProgress += studyProgressPercent;
            totalAnswerCount += answerCount;
            totalCorrectCount += correctCount;
            totalWrongCount += wrongCount;
            totalScore += scoreSum;
        }

        report.setStudentCount(studentCount);
        report.setAverageStudyProgress(studentCount == 0 ? 0 : totalStudyProgress / studentCount);

        report.setTotalAnswerCount(totalAnswerCount);
        report.setTotalCorrectCount(totalCorrectCount);
        report.setTotalWrongCount(totalWrongCount);
        report.setAverageQuizAccuracy(totalAnswerCount == 0 ? 0 : totalCorrectCount * 100 / totalAnswerCount);
        report.setAverageScore(totalAnswerCount == 0 ? 0 : totalScore / totalAnswerCount);

        report.setWeakKnowledgePoints(sortWeakPointMap(weakPointMap));
        report.setStudents(studentReports);
        report.setTeachingSuggestion(buildTeachingSuggestion(report));

        return report;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private Map<String, Integer> sortWeakPointMap(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        Map<String, Integer> result = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private String buildTeachingSuggestion(ClassLearningReportVO report) {
        StringBuilder builder = new StringBuilder();

        if (report.getStudentCount() == null || report.getStudentCount() == 0) {
            return "当前课程暂无学生数据。";
        }

        if (report.getAverageStudyProgress() != null && report.getAverageStudyProgress() < 60) {
            builder.append("班级平均学习完成率偏低，建议督促学生优先完成课程小节学习。");
        } else {
            builder.append("班级学习进度整体较好，可继续推进测验与巩固练习。");
        }

        if (report.getAverageQuizAccuracy() != null && report.getAverageQuizAccuracy() < 60) {
            builder.append(" 当前班级测验正确率偏低，建议教师安排针对性讲解。");
        } else {
            builder.append(" 当前班级测验正确率较稳定，可适当增加综合应用题训练。");
        }

        Map<String, Integer> weakMap = report.getWeakKnowledgePoints();

        if (weakMap != null && !weakMap.isEmpty()) {
            String topWeakPoint = weakMap.keySet().iterator().next();

            builder.append(" 班级主要薄弱知识点为【")
                    .append(topWeakPoint)
                    .append("】，建议在后续课堂中重点复习。");
        } else {
            builder.append(" 当前暂无明显集中薄弱知识点。");
        }

        return builder.toString();
    }
}