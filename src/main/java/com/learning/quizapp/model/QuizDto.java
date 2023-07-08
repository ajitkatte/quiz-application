package com.learning.quizapp.model;

import java.util.List;

public record QuizDto(Integer id, String title, List<QuizQuestion> questions) {
    
}
