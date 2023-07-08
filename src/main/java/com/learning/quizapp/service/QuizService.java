package com.learning.quizapp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.learning.quizapp.model.QuizDto;
import com.learning.quizapp.model.QuizQuestion;
import com.learning.quizapp.model.QuizSubmission;

public interface QuizService {
    ResponseEntity<Integer> createQuiz(String category, int numQ, String title);

    ResponseEntity<List<QuizQuestion>> getQuizQuestions(Integer id);

    ResponseEntity<Integer> calculateSubmissionScore(Integer quizId, List<QuizSubmission> submissions);

    ResponseEntity<QuizDto> getQuizById(Integer quizId);

    ResponseEntity<Boolean> deleteQuiz(Integer id);

    ResponseEntity<List<QuizDto>> getAll();

    ResponseEntity<QuizDto> updateQuizTitle(Integer id, String title);
}
