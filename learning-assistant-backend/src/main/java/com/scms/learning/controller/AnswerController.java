package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.AnswerRecord;
import com.scms.learning.entity.Question;
import com.scms.learning.entity.WrongQuestion;
import com.scms.learning.mapper.AnswerRecordMapper;
import com.scms.learning.mapper.QuestionMapper;
import com.scms.learning.mapper.WrongQuestionMapper;
import org.springframework.web.bind.annotation.*;
import com.scms.learning.vo.AnswerRecordDetailVO;
import com.scms.learning.entity.LearningTask;
import com.scms.learning.mapper.LearningTaskMapper;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@CrossOrigin
public class AnswerController {

    private final AnswerRecordMapper answerRecordMapper;
    private final QuestionMapper questionMapper;
    private final WrongQuestionMapper wrongQuestionMapper;
    private final LearningTaskMapper learningTaskMapper;

    public AnswerController(
            AnswerRecordMapper answerRecordMapper,
            QuestionMapper questionMapper,
            WrongQuestionMapper wrongQuestionMapper,
            LearningTaskMapper learningTaskMapper
    ) {
        this.answerRecordMapper = answerRecordMapper;
        this.questionMapper = questionMapper;
        this.wrongQuestionMapper = wrongQuestionMapper;
        this.learningTaskMapper = learningTaskMapper;
    }

    @GetMapping("/task/{taskId}/detail")
    public List<AnswerRecordDetailVO> getTaskAnswerRecordDetails(@PathVariable Long taskId) {
        return answerRecordMapper.selectAnswerRecordDetailsByTaskId(taskId);
    }

    @PostMapping("/submit")
    public AnswerRecord submitAnswer(@RequestBody AnswerRecord request) {
        if (request.getTaskId() == null) {
            throw new RuntimeException("任务ID不能为空");
        }

        if (request.getStudentId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }

        if (request.getQuestionId() == null) {
            throw new RuntimeException("题目ID不能为空");
        }

        if (request.getStudentAnswer() == null || request.getStudentAnswer().trim().isEmpty()) {
            throw new RuntimeException("学生答案不能为空");
        }

        Question question = questionMapper.selectById(request.getQuestionId());

        if (question == null) {
            throw new RuntimeException("题目不存在");
        }

        String correctAnswer = question.getAnswer();
        String studentAnswer = request.getStudentAnswer().trim();

        boolean correct = correctAnswer != null
                && correctAnswer.trim().equalsIgnoreCase(studentAnswer);

        LambdaQueryWrapper<AnswerRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnswerRecord::getTaskId, request.getTaskId())
                .eq(AnswerRecord::getQuestionId, request.getQuestionId())
                .eq(AnswerRecord::getStudentId, request.getStudentId())
                .orderByDesc(AnswerRecord::getSubmitTime)
                .orderByDesc(AnswerRecord::getId);

        List<AnswerRecord> oldRecords = answerRecordMapper.selectList(wrapper);

        AnswerRecord record;

        if (oldRecords == null || oldRecords.isEmpty()) {
            record = new AnswerRecord();
            record.setTaskId(request.getTaskId());
            record.setQuestionId(question.getId());
            record.setStudentId(request.getStudentId());
        } else {
            record = oldRecords.get(0);
        }

        record.setStudentAnswer(studentAnswer);
        record.setIsCorrect(correct ? 1 : 0);
        record.setScore(correct ? 100 : 0);
        record.setSubmitTime(LocalDateTime.now());

        record.setCourseId(question.getCourseId());
        record.setChapterId(question.getChapterId());
        record.setSectionId(question.getSectionId());
        record.setKnowledgePointId(question.getKnowledgePointId());

        if (request.getAnswerDuration() != null) {
            record.setAnswerDuration(request.getAnswerDuration());
        } else if (record.getAnswerDuration() == null) {
            record.setAnswerDuration(0);
        }

        if (record.getId() == null) {
            answerRecordMapper.insert(record);
        } else {
            answerRecordMapper.updateById(record);
        }

        if (oldRecords != null && oldRecords.size() > 1) {
            for (int i = 1; i < oldRecords.size(); i++) {
                answerRecordMapper.deleteById(oldRecords.get(i).getId());
            }
        }

        if (correct) {
            // 1. 如果是原错题再次答对，按 question_id 标记为已掌握
            markWrongQuestionMasteredIfExists(record, question);

            // 2. 如果是 AI 个性化复习任务答对，则按知识点标记相关错题为已掌握
            markRelatedWrongQuestionsMasteredForReviewTask(record, question);
        } else {
            saveOrUpdateWrongQuestion(record, question);
        }

        return record;
    }

    private void saveOrUpdateWrongQuestion(AnswerRecord record, Question question) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getStudentId, record.getStudentId())
                .eq(WrongQuestion::getQuestionId, record.getQuestionId())
                .orderByDesc(WrongQuestion::getId);

        List<WrongQuestion> wrongList = wrongQuestionMapper.selectList(wrapper);

        WrongQuestion wrongQuestion;

        if (wrongList == null || wrongList.isEmpty()) {
            wrongQuestion = new WrongQuestion();
            wrongQuestion.setStudentId(record.getStudentId());
            wrongQuestion.setQuestionId(record.getQuestionId());
            wrongQuestion.setWrongCount(1);
            wrongQuestion.setCreateTime(LocalDateTime.now());
        } else {
            wrongQuestion = wrongList.get(0);
            Integer oldCount = wrongQuestion.getWrongCount();
            wrongQuestion.setWrongCount(oldCount == null ? 1 : oldCount + 1);
        }

        wrongQuestion.setCourseId(question.getCourseId());
        wrongQuestion.setChapterId(question.getChapterId());
        wrongQuestion.setSectionId(question.getSectionId());
        wrongQuestion.setKnowledgePointId(question.getKnowledgePointId());
        wrongQuestion.setKnowledgePointName(question.getKnowledgePoint());
        wrongQuestion.setLastWrongTime(LocalDateTime.now());
        wrongQuestion.setIsMastered(0);
        wrongQuestion.setUpdateTime(LocalDateTime.now());

        if (wrongQuestion.getId() == null) {
            wrongQuestionMapper.insert(wrongQuestion);
        } else {
            wrongQuestionMapper.updateById(wrongQuestion);
        }

        if (wrongList != null && wrongList.size() > 1) {
            for (int i = 1; i < wrongList.size(); i++) {
                wrongQuestionMapper.deleteById(wrongList.get(i).getId());
            }
        }
    }

    private void markWrongQuestionMasteredIfExists(AnswerRecord record, Question question) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getStudentId, record.getStudentId())
                .eq(WrongQuestion::getQuestionId, record.getQuestionId())
                .orderByDesc(WrongQuestion::getId);

        List<WrongQuestion> wrongList = wrongQuestionMapper.selectList(wrapper);

        if (wrongList == null || wrongList.isEmpty()) {
            return;
        }

        WrongQuestion wrongQuestion = wrongList.get(0);
        wrongQuestion.setCourseId(question.getCourseId());
        wrongQuestion.setChapterId(question.getChapterId());
        wrongQuestion.setSectionId(question.getSectionId());
        wrongQuestion.setKnowledgePointId(question.getKnowledgePointId());
        wrongQuestion.setKnowledgePointName(question.getKnowledgePoint());
        wrongQuestion.setIsMastered(1);
        wrongQuestion.setUpdateTime(LocalDateTime.now());

        wrongQuestionMapper.updateById(wrongQuestion);

        if (wrongList.size() > 1) {
            for (int i = 1; i < wrongList.size(); i++) {
                wrongQuestionMapper.deleteById(wrongList.get(i).getId());
            }
        }
    }

    /**
     * 如果学生答对的是 review 类型复习任务中的题目，
     * 则根据该题知识点，把错题本中同课程、同知识点的未掌握错题标记为已掌握。
     */
    private void markRelatedWrongQuestionsMasteredForReviewTask(AnswerRecord record, Question question) {
        if (record == null || question == null) {
            return;
        }

        if (record.getTaskId() == null
                || record.getStudentId() == null
                || question.getCourseId() == null) {
            return;
        }

        if (!isReviewTask(record.getTaskId())) {
            return;
        }

        String questionKnowledgePoint = question.getKnowledgePoint();

        if (questionKnowledgePoint == null || questionKnowledgePoint.trim().isEmpty()) {
            return;
        }

        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(WrongQuestion::getStudentId, record.getStudentId())
                .eq(WrongQuestion::getCourseId, question.getCourseId())
                .eq(WrongQuestion::getIsMastered, 0)
                .orderByDesc(WrongQuestion::getLastWrongTime)
                .orderByDesc(WrongQuestion::getId);

        List<WrongQuestion> wrongList = wrongQuestionMapper.selectList(wrapper);

        if (wrongList == null || wrongList.isEmpty()) {
            return;
        }

        int updateCount = 0;

        for (WrongQuestion wrongQuestion : wrongList) {
            if (wrongQuestion == null) {
                continue;
            }

            String wrongKnowledgePoint = wrongQuestion.getKnowledgePointName();

            if (!sameKnowledgePoint(questionKnowledgePoint, wrongKnowledgePoint)) {
                continue;
            }

            wrongQuestion.setIsMastered(1);
            wrongQuestion.setUpdateTime(LocalDateTime.now());

            wrongQuestionMapper.updateById(wrongQuestion);
            updateCount++;
        }

        System.out.println("AI复习任务答对后，自动标记同知识点错题为已掌握，数量：" + updateCount);
    }

    private boolean isReviewTask(Long taskId) {
        if (taskId == null) {
            return false;
        }

        LearningTask task = learningTaskMapper.selectById(taskId);

        if (task == null || task.getTaskType() == null) {
            return false;
        }

        return "review".equalsIgnoreCase(task.getTaskType().trim());
    }

    private boolean sameKnowledgePoint(String questionKnowledgePoint, String wrongKnowledgePoint) {
        if (questionKnowledgePoint == null || wrongKnowledgePoint == null) {
            return false;
        }

        String q = questionKnowledgePoint.trim();
        String w = wrongKnowledgePoint.trim();

        if (q.isEmpty() || w.isEmpty()) {
            return false;
        }

        return q.equals(w) || q.contains(w) || w.contains(q);
    }

    @GetMapping("/student/{studentId}")
    public List<AnswerRecord> getStudentAnswerRecords(@PathVariable Long studentId) {
        LambdaQueryWrapper<AnswerRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(AnswerRecord::getStudentId, studentId)
                .orderByDesc(AnswerRecord::getSubmitTime);

        return answerRecordMapper.selectList(wrapper);
    }

    @GetMapping("/task/{taskId}")
    public List<AnswerRecord> getTaskAnswerRecords(@PathVariable Long taskId) {
        LambdaQueryWrapper<AnswerRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(AnswerRecord::getTaskId, taskId)
                .orderByAsc(AnswerRecord::getStudentId)
                .orderByAsc(AnswerRecord::getQuestionId);

        return answerRecordMapper.selectList(wrapper);
    }

    @GetMapping("/student/{studentId}/wrong")
    public List<AnswerRecordDetailVO> getStudentWrongAnswerDetails(@PathVariable Long studentId) {
        return answerRecordMapper.selectWrongAnswerDetailsByStudentId(studentId);
    }

    @GetMapping("/student/{studentId}/course/{courseId}/detail")
    public List<AnswerRecordDetailVO> getStudentAnswerDetailsByCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return answerRecordMapper.selectAnswerRecordDetailsByStudentIdAndCourseId(studentId, courseId);
    }

    @GetMapping("/student/{studentId}/course/{courseId}/wrong")
    public List<AnswerRecordDetailVO> getStudentWrongAnswerDetailsByCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return answerRecordMapper.selectWrongAnswerDetailsByStudentIdAndCourseId(studentId, courseId);
    }
}