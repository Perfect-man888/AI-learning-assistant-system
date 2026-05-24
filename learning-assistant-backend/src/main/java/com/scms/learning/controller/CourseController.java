package com.scms.learning.controller;

import com.scms.learning.common.Result;
import com.scms.learning.dto.CreateCourseRequest;
import com.scms.learning.dto.JoinCourseRequest;
import com.scms.learning.service.CourseService;
import com.scms.learning.vo.CourseVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public Result<CourseVO> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return Result.success(courseService.createCourse(request));
    }

    @GetMapping("/list")
    public Result<List<CourseVO>> listAllCourses() {
        return Result.success(courseService.listAllCourses());
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<CourseVO>> listTeacherCourses(@PathVariable Long teacherId) {
        return Result.success(courseService.listTeacherCourses(teacherId));
    }

    @PostMapping("/join")
    public Result<Void> joinCourse(@Valid @RequestBody JoinCourseRequest request) {
        courseService.joinCourse(request);
        return Result.success();
    }

    @GetMapping("/my/{studentId}")
    public Result<List<CourseVO>> listMyCourses(@PathVariable Long studentId) {
        return Result.success(courseService.listMyCourses(studentId));
    }
}