package com.learning.quizapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learning.quizapp.model.QuizDto;
import com.learning.quizapp.model.QuizQuestion;
import com.learning.quizapp.model.QuizSubmission;
import com.learning.quizapp.service.QuizService;

@RestController
@RequestMapping("quiz")
public class QuizController {

    private QuizService _quizService;

    public QuizController(QuizService quizService) {
        this._quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<Integer> createQuiz(@RequestParam String category, @RequestParam int numQ,
            @RequestParam String title) {
        return _quizService.createQuiz(category, numQ, title);
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAll() {
        return _quizService.getAll();
    }

    @GetMapping("questions/{id}")
    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Integer id) {
        return _quizService.getQuizQuestions(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Integer id) {
        return _quizService.getQuizById(id);
    }

    @PostMapping("submit/{quizId}")
    public ResponseEntity<Integer> submitQuizAndCalculateScore(@PathVariable Integer quizId,
            @RequestBody List<QuizSubmission> submissions) {
        return _quizService.calculateSubmissionScore(quizId, submissions);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteQuiz(@PathVariable Integer id) {
        return _quizService.deleteQuiz(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<QuizDto> updateQuizTitle(@PathVariable Integer id, @RequestParam String title) {
        return _quizService.updateQuizTitle(id, title);
    }
}
