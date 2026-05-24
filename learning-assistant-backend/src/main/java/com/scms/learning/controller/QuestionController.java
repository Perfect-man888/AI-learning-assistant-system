package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.Question;
import com.scms.learning.mapper.QuestionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin
public class QuestionController {

    private final QuestionMapper questionMapper;

    public QuestionController(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        if (question.getCourseId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        if (question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
            throw new RuntimeException("题目内容不能为空");
        }

        if (question.getAnswer() == null || question.getAnswer().trim().isEmpty()) {
            throw new RuntimeException("正确答案不能为空");
        }

        if (question.getQuestionType() == null || question.getQuestionType().trim().isEmpty()) {
            question.setQuestionType("single");
        }

        if (question.getDifficulty() == null || question.getDifficulty().trim().isEmpty()) {
            question.setDifficulty("normal");
        }

        question.setAnswer(question.getAnswer().trim().toUpperCase());
        question.setCreateTime(LocalDateTime.now());

        questionMapper.insert(question);
        return question;
    }

    @GetMapping("/course/{courseId}")
    public List<Question> getQuestionsByCourseId(@PathVariable Long courseId) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Question::getCourseId, courseId)
                .orderByAsc(Question::getId);

        return questionMapper.selectList(wrapper);
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionMapper.deleteById(id);
        return "删除成功";
    }
}