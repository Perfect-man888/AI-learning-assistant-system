package com.scms.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scms.learning.entity.AnswerRecord;
import com.scms.learning.vo.AnswerRecordDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerRecordMapper extends BaseMapper<AnswerRecord> {

    @Select("""
        SELECT
            ar.id AS id,
            ar.task_id AS taskId,
            ar.question_id AS questionId,
            ar.student_id AS studentId,
            u.real_name AS studentName,
            u.class_name AS className,
            q.question_text AS questionText,
            ar.student_answer AS studentAnswer,
            q.answer AS correctAnswer,
            q.knowledge_point AS knowledgePoint,
            ar.is_correct AS isCorrect,
            ar.score AS score,
            ar.submit_time AS submitTime
        FROM answer_record ar
        LEFT JOIN `user` u ON ar.student_id = u.id
        LEFT JOIN question q ON ar.question_id = q.id
        WHERE ar.task_id = #{taskId}
          AND ar.id IN (
              SELECT MAX(ar2.id)
              FROM answer_record ar2
              WHERE ar2.task_id = #{taskId}
              GROUP BY ar2.student_id, ar2.question_id
          )
        ORDER BY ar.student_id ASC, ar.question_id ASC, ar.submit_time DESC
        """)
    List<AnswerRecordDetailVO> selectAnswerRecordDetailsByTaskId(@Param("taskId") Long taskId);

    @Select("""
        SELECT
            ar.id AS id,
            ar.task_id AS taskId,
            ar.question_id AS questionId,
            ar.student_id AS studentId,
            u.real_name AS studentName,
            u.class_name AS className,
            q.question_text AS questionText,
            ar.student_answer AS studentAnswer,
            q.answer AS correctAnswer,
            q.knowledge_point AS knowledgePoint,
            ar.is_correct AS isCorrect,
            ar.score AS score,
            ar.submit_time AS submitTime
        FROM answer_record ar
        LEFT JOIN `user` u ON ar.student_id = u.id
        LEFT JOIN question q ON ar.question_id = q.id
        WHERE ar.student_id = #{studentId}
          AND ar.is_correct = 0
          AND ar.id IN (
              SELECT MAX(ar2.id)
              FROM answer_record ar2
              WHERE ar2.student_id = #{studentId}
              GROUP BY ar2.task_id, ar2.question_id, ar2.student_id
          )
        ORDER BY ar.submit_time DESC
        """)
    List<AnswerRecordDetailVO> selectWrongAnswerDetailsByStudentId(@Param("studentId") Long studentId);

    @Select("""
        SELECT
            ar.id AS id,
            ar.task_id AS taskId,
            ar.question_id AS questionId,
            ar.student_id AS studentId,
            u.real_name AS studentName,
            u.class_name AS className,
            q.question_text AS questionText,
            ar.student_answer AS studentAnswer,
            q.answer AS correctAnswer,
            q.knowledge_point AS knowledgePoint,
            ar.is_correct AS isCorrect,
            ar.score AS score,
            ar.submit_time AS submitTime
        FROM answer_record ar
        LEFT JOIN `user` u ON ar.student_id = u.id
        LEFT JOIN question q ON ar.question_id = q.id
        LEFT JOIN learning_task t ON ar.task_id = t.id
        WHERE ar.student_id = #{studentId}
          AND t.course_id = #{courseId}
          AND ar.id IN (
              SELECT MAX(ar2.id)
              FROM answer_record ar2
              LEFT JOIN learning_task t2 ON ar2.task_id = t2.id
              WHERE ar2.student_id = #{studentId}
                AND t2.course_id = #{courseId}
              GROUP BY ar2.task_id, ar2.question_id, ar2.student_id
          )
        ORDER BY ar.submit_time DESC
        """)
    List<AnswerRecordDetailVO> selectAnswerRecordDetailsByStudentIdAndCourseId(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId
    );

    @Select("""
        SELECT
            ar.id AS id,
            ar.task_id AS taskId,
            ar.question_id AS questionId,
            ar.student_id AS studentId,
            u.real_name AS studentName,
            u.class_name AS className,
            q.question_text AS questionText,
            ar.student_answer AS studentAnswer,
            q.answer AS correctAnswer,
            q.knowledge_point AS knowledgePoint,
            ar.is_correct AS isCorrect,
            ar.score AS score,
            ar.submit_time AS submitTime
        FROM answer_record ar
        LEFT JOIN `user` u ON ar.student_id = u.id
        LEFT JOIN question q ON ar.question_id = q.id
        LEFT JOIN learning_task t ON ar.task_id = t.id
        WHERE ar.student_id = #{studentId}
          AND t.course_id = #{courseId}
          AND ar.is_correct = 0
          AND ar.id IN (
              SELECT MAX(ar2.id)
              FROM answer_record ar2
              LEFT JOIN learning_task t2 ON ar2.task_id = t2.id
              WHERE ar2.student_id = #{studentId}
                AND t2.course_id = #{courseId}
              GROUP BY ar2.task_id, ar2.question_id, ar2.student_id
          )
        ORDER BY ar.submit_time DESC
        """)
    List<AnswerRecordDetailVO> selectWrongAnswerDetailsByStudentIdAndCourseId(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId
    );
}