package com.scms.learning.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scms.learning.dto.ReviewTaskGenerateRequest;
import com.scms.learning.entity.LearningTask;
import com.scms.learning.entity.LearningTaskQuestion;
import com.scms.learning.entity.Question;
import com.scms.learning.entity.WrongQuestion;
import com.scms.learning.mapper.LearningTaskMapper;
import com.scms.learning.mapper.LearningTaskQuestionMapper;
import com.scms.learning.mapper.QuestionMapper;
import com.scms.learning.mapper.WrongQuestionMapper;
import org.springframework.web.bind.annotation.*;
import com.scms.learning.service.AiQuestionService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/review-tasks")
@CrossOrigin
public class ReviewTaskController {

    private final WrongQuestionMapper wrongQuestionMapper;
    private final QuestionMapper questionMapper;
    private final LearningTaskMapper learningTaskMapper;
    private final LearningTaskQuestionMapper learningTaskQuestionMapper;
    private final AiQuestionService aiQuestionService;

    public ReviewTaskController(
            WrongQuestionMapper wrongQuestionMapper,
            QuestionMapper questionMapper,
            LearningTaskMapper learningTaskMapper,
            LearningTaskQuestionMapper learningTaskQuestionMapper,
            AiQuestionService aiQuestionService
    ) {
        this.wrongQuestionMapper = wrongQuestionMapper;
        this.questionMapper = questionMapper;
        this.learningTaskMapper = learningTaskMapper;
        this.learningTaskQuestionMapper = learningTaskQuestionMapper;
        this.aiQuestionService = aiQuestionService;
    }
    /**
     * 根据学生错题本自动生成个性化复习任务。
     * 规则：优先选择未掌握错题本中的原题，再补充相同知识点下的同类题。
     */
    @PostMapping("/generate")
    public LearningTask generateReviewTask(@RequestBody ReviewTaskGenerateRequest request) {
        validateRequest(request);

        int targetCount = request.getCount() == null ? 5 : request.getCount();

        if (targetCount <= 0) {
            targetCount = 5;
        }

        if (targetCount > 10) {
            targetCount = 10;
        }

        List<WrongQuestion> wrongQuestions = loadUnmasteredWrongQuestions(
                request.getStudentId(),
                request.getCourseId()
        );

        if (wrongQuestions.isEmpty()) {
            throw new RuntimeException("当前课程暂无未掌握错题，暂时无法生成个性化复习任务");
        }

        List<Question> recommendedQuestions = aiQuestionService.generateSimilarQuestionsByWrongBook(
                request.getCourseId(),
                wrongQuestions,
                targetCount
        );

        if (recommendedQuestions.isEmpty()) {
            throw new RuntimeException("没有找到可用于复习的题目，请教师先为该课程创建题目");
        }

        LearningTask task = createReviewTask(request, recommendedQuestions, wrongQuestions);

        bindQuestionsToTask(task.getId(), recommendedQuestions);

        return task;
    }

    private void validateRequest(ReviewTaskGenerateRequest request) {
        if (request == null) {
            throw new RuntimeException("请求参数不能为空");
        }

        if (request.getStudentId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }

        if (request.getCourseId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }
    }

    private List<WrongQuestion> loadUnmasteredWrongQuestions(Long studentId, Long courseId) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(WrongQuestion::getStudentId, studentId)
                .eq(WrongQuestion::getCourseId, courseId)
                .orderByDesc(WrongQuestion::getWrongCount)
                .orderByDesc(WrongQuestion::getLastWrongTime);

        List<WrongQuestion> all = wrongQuestionMapper.selectList(wrapper);
        List<WrongQuestion> result = new ArrayList<>();

        for (WrongQuestion wrongQuestion : all) {
            Integer mastered = wrongQuestion.getIsMastered();

            if (mastered == null || mastered == 0) {
                result.add(wrongQuestion);
            }
        }

        return result;
    }

    private List<Question> recommendQuestionsByWrongBook(
            Long courseId,
            List<WrongQuestion> wrongQuestions,
            int targetCount
    ) {
        Map<Long, Question> selectedMap = new LinkedHashMap<>();
        Set<String> weakKnowledgePoints = new LinkedHashSet<>();

        // 第一步：优先加入学生真实答错过的原题
        for (WrongQuestion wrongQuestion : wrongQuestions) {
            if (wrongQuestion.getKnowledgePointName() != null
                    && !wrongQuestion.getKnowledgePointName().trim().isEmpty()) {
                weakKnowledgePoints.add(wrongQuestion.getKnowledgePointName().trim());
            }

            if (wrongQuestion.getQuestionId() == null) {
                continue;
            }

            Question question = questionMapper.selectById(wrongQuestion.getQuestionId());

            if (question != null && question.getId() != null) {
                selectedMap.put(question.getId(), question);
            }

            if (selectedMap.size() >= targetCount) {
                return new ArrayList<>(selectedMap.values());
            }
        }

        // 第二步：补充同一课程、同一薄弱知识点下的题目
        LambdaQueryWrapper<Question> questionWrapper = new LambdaQueryWrapper<>();

        questionWrapper.eq(Question::getCourseId, courseId)
                .orderByDesc(Question::getCreateTime)
                .orderByDesc(Question::getId);

        List<Question> courseQuestions = questionMapper.selectList(questionWrapper);

        for (Question question : courseQuestions) {
            if (question == null || question.getId() == null) {
                continue;
            }

            if (selectedMap.containsKey(question.getId())) {
                continue;
            }

            if (matchKnowledgePoint(question.getKnowledgePoint(), weakKnowledgePoints)) {
                selectedMap.put(question.getId(), question);
            }

            if (selectedMap.size() >= targetCount) {
                break;
            }
        }

        // 第三步：同知识点题目不足时，再补充本课程其他题目
        if (selectedMap.size() < targetCount) {
            for (Question question : courseQuestions) {
                if (question == null || question.getId() == null) {
                    continue;
                }

                selectedMap.putIfAbsent(question.getId(), question);

                if (selectedMap.size() >= targetCount) {
                    break;
                }
            }
        }

        return new ArrayList<>(selectedMap.values());
    }

    private boolean matchKnowledgePoint(String questionKnowledgePoint, Set<String> weakKnowledgePoints) {
        if (weakKnowledgePoints == null || weakKnowledgePoints.isEmpty()) {
            return false;
        }

        if (questionKnowledgePoint == null || questionKnowledgePoint.trim().isEmpty()) {
            return false;
        }

        String questionPoint = questionKnowledgePoint.trim();

        for (String weakPoint : weakKnowledgePoints) {
            if (weakPoint == null || weakPoint.trim().isEmpty()) {
                continue;
            }

            String weak = weakPoint.trim();

            if (questionPoint.equals(weak)
                    || questionPoint.contains(weak)
                    || weak.contains(questionPoint)) {
                return true;
            }
        }

        return false;
    }

    private LearningTask createReviewTask(
            ReviewTaskGenerateRequest request,
            List<Question> recommendedQuestions,
            List<WrongQuestion> wrongQuestions
    ) {
        Question firstQuestion = recommendedQuestions.get(0);

        LearningTask task = new LearningTask();

        task.setCourseId(request.getCourseId());
        task.setChapterId(firstQuestion.getChapterId());
        task.setSectionId(firstQuestion.getSectionId());

        // 这里先固定为 1，表示系统自动生成任务。
        // 后续可以改成真实教师ID。
        task.setTeacherId(1L);

        task.setTaskTitle("个性化复习任务-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));
        task.setTaskType("review");
        task.setStatus("published");
        task.setDescription(buildDescription(wrongQuestions, recommendedQuestions));
        task.setCreateTime(LocalDateTime.now());

        learningTaskMapper.insert(task);

        return task;
    }

    private String buildDescription(List<WrongQuestion> wrongQuestions, List<Question> recommendedQuestions) {
        Set<String> weakPoints = new LinkedHashSet<>();

        for (WrongQuestion wrongQuestion : wrongQuestions) {
            if (wrongQuestion.getKnowledgePointName() != null
                    && !wrongQuestion.getKnowledgePointName().trim().isEmpty()) {
                weakPoints.add(wrongQuestion.getKnowledgePointName().trim());
            }
        }

        return "系统根据学生错题本调用AI自动生成相似复习题。薄弱知识点："
                + (weakPoints.isEmpty() ? "未标注" : String.join("、", weakPoints))
                + "；AI生成题目数量：" + recommendedQuestions.size() + "道。";
    }

    private void bindQuestionsToTask(Long taskId, List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            if (question == null || question.getId() == null) {
                continue;
            }

            LearningTaskQuestion relation = new LearningTaskQuestion();

            relation.setTaskId(taskId);
            relation.setQuestionId(question.getId());
            relation.setSortOrder(i + 1);
            relation.setCreateTime(LocalDateTime.now());

            learningTaskQuestionMapper.insert(relation);
        }
    }
}