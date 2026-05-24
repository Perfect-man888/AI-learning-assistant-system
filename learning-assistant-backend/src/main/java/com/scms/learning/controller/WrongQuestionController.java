package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.WrongQuestion;
import com.scms.learning.mapper.WrongQuestionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/wrong-questions")
@CrossOrigin
public class WrongQuestionController {

    private final WrongQuestionMapper wrongQuestionMapper;

    public WrongQuestionController(WrongQuestionMapper wrongQuestionMapper) {
        this.wrongQuestionMapper = wrongQuestionMapper;
    }

    @GetMapping("/student/{studentId}")
    public List<WrongQuestion> getByStudentId(@PathVariable Long studentId) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getStudentId, studentId)
                .orderByAsc(WrongQuestion::getIsMastered)
                .orderByDesc(WrongQuestion::getLastWrongTime);

        return wrongQuestionMapper.selectList(wrapper);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public List<WrongQuestion> getByStudentIdAndCourseId(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getStudentId, studentId)
                .eq(WrongQuestion::getCourseId, courseId)
                .orderByAsc(WrongQuestion::getIsMastered)
                .orderByDesc(WrongQuestion::getLastWrongTime);

        return wrongQuestionMapper.selectList(wrapper);
    }

    @PutMapping("/{id}/mastered")
    public String markMastered(@PathVariable Long id) {
        WrongQuestion wrongQuestion = wrongQuestionMapper.selectById(id);

        if (wrongQuestion == null) {
            throw new RuntimeException("错题记录不存在");
        }

        wrongQuestion.setIsMastered(1);
        wrongQuestion.setUpdateTime(LocalDateTime.now());

        wrongQuestionMapper.updateById(wrongQuestion);

        return "已标记掌握";
    }

    @DeleteMapping("/{id}")
    public String deleteWrongQuestion(@PathVariable Long id) {
        wrongQuestionMapper.deleteById(id);
        return "删除成功";
    }
}