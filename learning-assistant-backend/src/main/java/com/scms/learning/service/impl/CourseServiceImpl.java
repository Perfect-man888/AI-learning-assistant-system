package com.scms.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.dto.CreateCourseRequest;
import com.scms.learning.dto.JoinCourseRequest;
import com.scms.learning.entity.Course;
import com.scms.learning.entity.CourseMember;
import com.scms.learning.entity.User;
import com.scms.learning.mapper.CourseMapper;
import com.scms.learning.mapper.CourseMemberMapper;
import com.scms.learning.mapper.UserMapper;
import com.scms.learning.service.CourseService;
import com.scms.learning.vo.CourseVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    private final CourseMemberMapper courseMemberMapper;

    private final UserMapper userMapper;

    public CourseServiceImpl(
            CourseMapper courseMapper,
            CourseMemberMapper courseMemberMapper,
            UserMapper userMapper
    ) {
        this.courseMapper = courseMapper;
        this.courseMemberMapper = courseMemberMapper;
        this.userMapper = userMapper;
    }

    @Override
    public CourseVO createCourse(CreateCourseRequest request) {
        User teacher = userMapper.selectById(request.getTeacherId());

        if (teacher == null) {
            throw new RuntimeException("教师用户不存在");
        }

        if (!"teacher".equals(teacher.getRole())) {
            throw new RuntimeException("只有教师可以创建课程");
        }

        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setTeacherId(teacher.getId());
        course.setTeacherName(teacher.getRealName());
        course.setClassName(teacher.getClassName());

        courseMapper.insert(course);

        return toVO(course, false);
    }

    @Override
    public List<CourseVO> listAllCourses() {
        List<Course> courses = courseMapper.selectList(
                new LambdaQueryWrapper<Course>()
                        .orderByDesc(Course::getCreateTime)
        );

        List<CourseVO> result = new ArrayList<>();

        for (Course course : courses) {
            result.add(toVO(course, false));
        }

        return result;
    }

    @Override
    public List<CourseVO> listTeacherCourses(Long teacherId) {
        List<Course> courses = courseMapper.selectList(
                new LambdaQueryWrapper<Course>()
                        .eq(Course::getTeacherId, teacherId)
                        .orderByDesc(Course::getCreateTime)
        );

        List<CourseVO> result = new ArrayList<>();

        for (Course course : courses) {
            result.add(toVO(course, false));
        }

        return result;
    }

    @Override
    public void joinCourse(JoinCourseRequest request) {
        User student = userMapper.selectById(request.getStudentId());

        if (student == null) {
            throw new RuntimeException("学生用户不存在");
        }

        if (!"student".equals(student.getRole())) {
            throw new RuntimeException("只有学生可以加入课程");
        }

        Course course = courseMapper.selectById(request.getCourseId());

        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        CourseMember exist = courseMemberMapper.selectOne(
                new LambdaQueryWrapper<CourseMember>()
                        .eq(CourseMember::getCourseId, request.getCourseId())
                        .eq(CourseMember::getStudentId, request.getStudentId())
        );

        if (exist != null) {
            throw new RuntimeException("你已经加入过该课程");
        }

        CourseMember member = new CourseMember();
        member.setCourseId(request.getCourseId());
        member.setStudentId(request.getStudentId());

        courseMemberMapper.insert(member);
    }

    @Override
    public List<CourseVO> listMyCourses(Long studentId) {
        List<CourseMember> members = courseMemberMapper.selectList(
                new LambdaQueryWrapper<CourseMember>()
                        .eq(CourseMember::getStudentId, studentId)
                        .orderByDesc(CourseMember::getJoinTime)
        );

        List<CourseVO> result = new ArrayList<>();

        for (CourseMember member : members) {
            Course course = courseMapper.selectById(member.getCourseId());

            if (course != null) {
                result.add(toVO(course, true));
            }
        }

        return result;
    }

    private CourseVO toVO(Course course, boolean joined) {
        CourseVO vo = new CourseVO();
        vo.setId(course.getId());
        vo.setCourseName(course.getCourseName());
        vo.setDescription(course.getDescription());
        vo.setTeacherId(course.getTeacherId());
        vo.setTeacherName(course.getTeacherName());
        vo.setClassName(course.getClassName());
        vo.setCreateTime(course.getCreateTime());
        vo.setJoined(joined);
        return vo;
    }
}