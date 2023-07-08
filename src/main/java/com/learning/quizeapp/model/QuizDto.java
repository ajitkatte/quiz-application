package com.learning.quizeapp.model;

import java.util.List;

public record QuizDto(Integer id, String title, List<QuizQuestion> questions) {
    
}
