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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@CrossOrigin
public class AnswerController {

    private final AnswerRecordMapper answerRecordMapper;
    private final QuestionMapper questionMapper;
    private final WrongQuestionMapper wrongQuestionMapper;

    public AnswerController(
            AnswerRecordMapper answerRecordMapper,
            QuestionMapper questionMapper,
            WrongQuestionMapper wrongQuestionMapper
    ) {
        this.answerRecordMapper = answerRecordMapper;
        this.questionMapper = questionMapper;
        this.wrongQuestionMapper = wrongQuestionMapper;
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
            markWrongQuestionMasteredIfExists(record, question);
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