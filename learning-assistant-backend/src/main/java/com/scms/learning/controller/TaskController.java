package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.entity.LearningTask;
import com.scms.learning.mapper.LearningTaskMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final LearningTaskMapper learningTaskMapper;

    public TaskController(LearningTaskMapper learningTaskMapper) {
        this.learningTaskMapper = learningTaskMapper;
    }

    @PostMapping
    public LearningTask createTask(@RequestBody LearningTask task) {
        if (task.getCourseId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        if (task.getTeacherId() == null) {
            task.setTeacherId(1L);
        }

        if (task.getTaskTitle() == null || task.getTaskTitle().trim().isEmpty()) {
            task.setTaskTitle("课程测验");
        }

        if (task.getTaskType() == null || task.getTaskType().trim().isEmpty()) {
            task.setTaskType("quiz");
        }

        task.setCreateTime(LocalDateTime.now());

        learningTaskMapper.insert(task);
        return task;
    }

    @GetMapping("/course/{courseId}")
    public List<LearningTask> getTasksByCourseId(@PathVariable Long courseId) {
        LambdaQueryWrapper<LearningTask> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(LearningTask::getCourseId, courseId)
                .orderByDesc(LearningTask::getCreateTime);

        return learningTaskMapper.selectList(wrapper);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        if (id == null) {
            throw new RuntimeException("任务ID不能为空");
        }

        learningTaskMapper.deleteById(id);
        return "删除成功";
    }

}