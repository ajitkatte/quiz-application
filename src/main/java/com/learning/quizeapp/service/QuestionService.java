package com.learning.quizeapp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.learning.quizeapp.model.Question;

public interface QuestionService {
   ResponseEntity<List<Question>> getAllQuestions();

   ResponseEntity<List<Question>> getQuestionsByCategory(String category);

   ResponseEntity<String> addQuestion(Question question);

   ResponseEntity<Boolean> deleteQuestion(int id);

   ResponseEntity<Question> getQuestionById(int id);
}
