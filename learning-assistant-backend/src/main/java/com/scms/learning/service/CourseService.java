package com.scms.learning.service;

import com.scms.learning.dto.CreateCourseRequest;
import com.scms.learning.dto.JoinCourseRequest;
import com.scms.learning.vo.CourseVO;

import java.util.List;

public interface CourseService {

    CourseVO createCourse(CreateCourseRequest request);

    List<CourseVO> listAllCourses();

    List<CourseVO> listTeacherCourses(Long teacherId);

    void joinCourse(JoinCourseRequest request);

    List<CourseVO> listMyCourses(Long studentId);
}