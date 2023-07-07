package com.learning.quizeapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learning.quizeapp.model.QuizQuestion;
import com.learning.quizeapp.model.QuizSubmission;
import com.learning.quizeapp.service.QuizService;


@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private QuizService _quizService;

    @PostMapping
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ, @RequestParam String title) {
        return _quizService.createQuiz(category, numQ, title);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Integer id) {
        return _quizService.getQuizQuestions(id);
    }

    @PostMapping("submit/{quizId}")
    public ResponseEntity<Integer> submitQuizAndCalculateScore(@PathVariable Integer quizId, @RequestBody List<QuizSubmission> submissions) {
        return _quizService.calculateSubmissionScore(quizId, submissions);  
    }

}
