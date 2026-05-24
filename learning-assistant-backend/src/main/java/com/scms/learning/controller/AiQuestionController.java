package com.scms.learning.controller;

import com.scms.learning.dto.AiGenerateQuestionRequest;
import com.scms.learning.entity.Question;
import com.scms.learning.service.AiQuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/questions")
@CrossOrigin
public class AiQuestionController {

    private final AiQuestionService aiQuestionService;

    public AiQuestionController(AiQuestionService aiQuestionService) {
        this.aiQuestionService = aiQuestionService;
    }

    @PostMapping("/generate")
    public List<Question> generateQuestions(@RequestBody AiGenerateQuestionRequest request) {
        return aiQuestionService.generateAndSaveQuestions(request);
    }
}