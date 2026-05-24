package com.scms.learning.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learning.dto.AiGenerateQuestionRequest;
import com.scms.learning.entity.Question;
import com.scms.learning.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiQuestionService {

    private static final String DASHSCOPE_URL =
            "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    // 先用 qwen-plus 跑通功能，后面再改成 qwen3.7-max
    private static final String MODEL_NAME = "qwen-plus";

    private final QuestionMapper questionMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public AiQuestionService(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    public List<Question> generateAndSaveQuestions(AiGenerateQuestionRequest request) {
        validateRequest(request);

        try {
            String apiKey = System.getenv("DASHSCOPE_API_KEY");

            System.out.println("DASHSCOPE_API_KEY 是否存在：" + (apiKey != null && !apiKey.trim().isEmpty()));

            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new RuntimeException("未配置环境变量 DASHSCOPE_API_KEY");
            }

            String prompt = buildPrompt(request);
            String requestJson = buildRequestJson(prompt);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(DASHSCOPE_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException(
                        "AI接口调用失败，状态码：" + response.statusCode()
                                + "，返回内容：" + response.body()
                );
            }

            String aiContent = extractAiContent(response.body());
            List<Question> questions = parseQuestions(aiContent, request);

            for (Question question : questions) {
                questionMapper.insert(question);
            }

            return questions;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AI生成题目失败：" + e.getMessage());
        }
    }

    private void validateRequest(AiGenerateQuestionRequest request) {
        if (request.getCourseId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        if (request.getChapterId() == null) {
            throw new RuntimeException("章节ID不能为空");
        }

        if (request.getSectionId() == null) {
            throw new RuntimeException("小节ID不能为空");
        }

        if (request.getKnowledgePoint() == null || request.getKnowledgePoint().trim().isEmpty()) {
            throw new RuntimeException("知识点不能为空");
        }

        if (request.getQuestionType() == null || request.getQuestionType().trim().isEmpty()) {
            request.setQuestionType("single");
        }

        if (request.getDifficulty() == null || request.getDifficulty().trim().isEmpty()) {
            request.setDifficulty("easy");
        }

        if (request.getCount() == null || request.getCount() <= 0) {
            request.setCount(2);
        }

        if (request.getCount() > 10) {
            request.setCount(10);
        }
    }

    private String buildPrompt(AiGenerateQuestionRequest request) {
        return """
                你是一名大学课程教师，请根据给定知识点生成课程测验题。
                
                要求：
                1. 只生成单选题。
                2. 每道题必须有 A、B、C、D 四个选项。
                3. 正确答案只能是 A、B、C、D 之一。
                4. 题目适合课堂测验，语言清晰，难度符合要求。
                5. 输出必须是严格 JSON 数组。
                6. 不要输出 Markdown。
                7. 不要输出 ```json。
                8. 不要输出任何解释性文字。
                
                请严格按照下面 JSON 数组格式返回：
                [
                  {
                    "questionText": "题目内容",
                    "options": "A. 选项A\\nB. 选项B\\nC. 选项C\\nD. 选项D",
                    "answer": "A",
                    "analysis": "解析内容",
                    "knowledgePoint": "知识点",
                    "difficulty": "easy"
                  }
                ]
                
                生成要求：
                课程ID：%d
                章节ID：%d
                小节ID：%d
                知识点：%s
                题型：%s
                难度：%s
                题目数量：%d
                """.formatted(
                request.getCourseId(),
                request.getChapterId(),
                request.getSectionId(),
                request.getKnowledgePoint(),
                request.getQuestionType(),
                request.getDifficulty(),
                request.getCount()
        );
    }

    private String buildRequestJson(String prompt) throws Exception {
        String escapedPrompt = objectMapper.writeValueAsString(prompt);

        return """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "system",
                      "content": "你是一名严谨的大学教师。你必须只返回合法JSON数组，不要返回Markdown。"
                    },
                    {
                      "role": "user",
                      "content": %s
                    }
                  ],
                  "temperature": 0.7
                }
                """.formatted(MODEL_NAME, escapedPrompt);
    }

    private String extractAiContent(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);

        JsonNode contentNode = root
                .path("choices")
                .path(0)
                .path("message")
                .path("content");

        if (contentNode.isMissingNode() || contentNode.asText().trim().isEmpty()) {
            throw new RuntimeException("AI返回内容为空：" + responseBody);
        }

        return cleanJsonText(contentNode.asText());
    }

    private String cleanJsonText(String text) {
        if (text == null) {
            return "";
        }

        String result = text.trim();

        if (result.startsWith("```json")) {
            result = result.substring("```json".length()).trim();
        }

        if (result.startsWith("```")) {
            result = result.substring("```".length()).trim();
        }

        if (result.endsWith("```")) {
            result = result.substring(0, result.length() - 3).trim();
        }

        int arrayStart = result.indexOf("[");
        int arrayEnd = result.lastIndexOf("]");

        if (arrayStart >= 0 && arrayEnd > arrayStart) {
            result = result.substring(arrayStart, arrayEnd + 1);
        }

        return result;
    }

    private List<Question> parseQuestions(
            String aiContent,
            AiGenerateQuestionRequest request
    ) throws Exception {
        JsonNode arrayNode = objectMapper.readTree(aiContent);

        if (!arrayNode.isArray()) {
            throw new RuntimeException("AI返回的不是JSON数组：" + aiContent);
        }

        List<Question> questions = new ArrayList<>();

        for (JsonNode node : arrayNode) {
            String questionText = node.path("questionText").asText();
            String options = node.path("options").asText();
            String answer = node.path("answer").asText();

            if (questionText == null || questionText.trim().isEmpty()) {
                continue;
            }

            if (answer == null || answer.trim().isEmpty()) {
                continue;
            }

            Question question = new Question();

            question.setCourseId(request.getCourseId());
            question.setChapterId(request.getChapterId());
            question.setSectionId(request.getSectionId());

            question.setQuestionType("single");
            question.setQuestionText(questionText.trim());
            question.setOptions(options == null ? "" : options.trim());
            question.setAnswer(answer.trim().toUpperCase());
            question.setAnalysis(node.path("analysis").asText(""));
            question.setKnowledgePoint(node.path("knowledgePoint").asText(request.getKnowledgePoint()));
            question.setDifficulty(node.path("difficulty").asText(request.getDifficulty()));
            question.setCreateTime(LocalDateTime.now());

            questions.add(question);
        }

        if (questions.isEmpty()) {
            throw new RuntimeException("AI没有生成有效题目，原始内容：" + aiContent);
        }

        return questions;
    }
}