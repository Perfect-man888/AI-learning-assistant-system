package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.LearningTaskQuestion;
import com.scms.learning.entity.Question;
import com.scms.learning.mapper.LearningTaskQuestionMapper;
import com.scms.learning.mapper.QuestionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/task-questions")
@CrossOrigin
public class TaskQuestionController {

    private final LearningTaskQuestionMapper learningTaskQuestionMapper;
    private final QuestionMapper questionMapper;

    public TaskQuestionController(
            LearningTaskQuestionMapper learningTaskQuestionMapper,
            QuestionMapper questionMapper
    ) {
        this.learningTaskQuestionMapper = learningTaskQuestionMapper;
        this.questionMapper = questionMapper;
    }

    @PostMapping
    public LearningTaskQuestion addQuestionToTask(@RequestBody LearningTaskQuestion taskQuestion) {
        if (taskQuestion.getTaskId() == null) {
            throw new RuntimeException("任务ID不能为空");
        }

        if (taskQuestion.getQuestionId() == null) {
            throw new RuntimeException("题目ID不能为空");
        }

        LambdaQueryWrapper<LearningTaskQuestion> existsWrapper = new LambdaQueryWrapper<>();
        existsWrapper.eq(LearningTaskQuestion::getTaskId, taskQuestion.getTaskId())
                .eq(LearningTaskQuestion::getQuestionId, taskQuestion.getQuestionId());

        Long count = learningTaskQuestionMapper.selectCount(existsWrapper);
        if (count != null && count > 0) {
            throw new RuntimeException("该题目已经加入任务");
        }

        if (taskQuestion.getSortOrder() == null) {
            taskQuestion.setSortOrder(0);
        }

        taskQuestion.setCreateTime(LocalDateTime.now());
        learningTaskQuestionMapper.insert(taskQuestion);

        return taskQuestion;
    }

    @GetMapping("/task/{taskId}")
    public List<Question> getQuestionsByTaskId(@PathVariable Long taskId) {
        LambdaQueryWrapper<LearningTaskQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningTaskQuestion::getTaskId, taskId)
                .orderByAsc(LearningTaskQuestion::getSortOrder)
                .orderByAsc(LearningTaskQuestion::getId);

        List<LearningTaskQuestion> relations = learningTaskQuestionMapper.selectList(wrapper);

        List<Long> questionIds = relations.stream()
                .map(LearningTaskQuestion::getQuestionId)
                .toList();

        if (questionIds.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Question> questionWrapper = new LambdaQueryWrapper<>();
        questionWrapper.in(Question::getId, questionIds);

        return questionMapper.selectList(questionWrapper);
    }

    @DeleteMapping("/task/{taskId}/question/{questionId}")
    public String removeQuestionFromTask(
            @PathVariable Long taskId,
            @PathVariable Long questionId
    ) {
        LambdaQueryWrapper<LearningTaskQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningTaskQuestion::getTaskId, taskId)
                .eq(LearningTaskQuestion::getQuestionId, questionId);

        learningTaskQuestionMapper.delete(wrapper);
        return "移除成功";
    }
}