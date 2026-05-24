package com.scms.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scms.learning.entity.StudyProgress;
import com.scms.learning.vo.StudentProgressSummaryVO;
import com.scms.learning.vo.StudyProgressDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudyProgressMapper extends BaseMapper<StudyProgress> {

    @Select("""
            SELECT
                u.id AS studentId,
                u.real_name AS studentName,
                u.class_name AS className,
                cm.course_id AS courseId,

                (
                    SELECT COUNT(*)
                    FROM section s
                    INNER JOIN chapter ch ON s.chapter_id = ch.id
                    WHERE ch.course_id = cm.course_id
                ) AS totalSections,

                (
                    SELECT COUNT(DISTINCT sp.section_id)
                    FROM study_progress sp
                    WHERE sp.course_id = cm.course_id
                      AND sp.student_id = cm.student_id
                      AND sp.status = 'completed'
                ) AS completedSections

            FROM course_member cm
            INNER JOIN `user` u ON cm.student_id = u.id
            WHERE cm.course_id = #{courseId}
            ORDER BY u.id
            """)
    List<StudentProgressSummaryVO> selectCourseProgressSummary(@Param("courseId") Long courseId);


    @Select("""
            SELECT
                sp.id AS id,
                ch.course_id AS courseId,
                ch.id AS chapterId,
                s.id AS sectionId,
                #{studentId} AS studentId,
                ch.chapter_title AS chapterTitle,
                s.section_title AS sectionTitle,
                COALESCE(sp.status, 'not_started') AS status,
                COALESCE(sp.progress_percent, 0) AS progressPercent,
                sp.update_time AS updateTime
            FROM section s
            INNER JOIN chapter ch ON s.chapter_id = ch.id
            LEFT JOIN study_progress sp
                ON sp.section_id = s.id
               AND sp.chapter_id = ch.id
               AND sp.course_id = ch.course_id
               AND sp.student_id = #{studentId}
            WHERE ch.course_id = #{courseId}
            ORDER BY ch.sort_order ASC, s.sort_order ASC, ch.id ASC, s.id ASC
            """)
    List<StudyProgressDetailVO> selectStudentCourseProgressDetail(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId
    );
}