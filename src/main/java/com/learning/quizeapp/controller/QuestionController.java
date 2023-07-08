package com.learning.quizeapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learning.quizeapp.model.Question;
import com.learning.quizeapp.service.QuestionService;

@RestController
@RequestMapping("questions")
public class QuestionController {

    private QuestionService _questionService;

    public QuestionController(QuestionService questionService) {
        this._questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return _questionService.getAllQuestions();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return _questionService.getQuestionsByCategory(category);
    }

    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return _questionService.addQuestion(question);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteQuestion(@PathVariable int id) {
        return _questionService.deleteQuestion(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
        return _questionService.getQuestionById(id);
    }
}
