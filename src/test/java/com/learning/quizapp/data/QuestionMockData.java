package com.learning.quizapp.data;

import java.util.ArrayList;
import java.util.List;

import com.learning.quizapp.model.Question;

public class QuestionMockData {

    public static List<Question> getMockQuestions() {
        var question = new Question();
        question.setId(1);
        question.setQuestionTitle("When java code is comipled it generated byte-code.");
        question.setCategory("Java");
        question.setDifficultyLevel("Medium");
        question.setAnswer("true");
        ArrayList<Question> questions = new ArrayList<>() {
            {
                add(question);
            }
        };
        return questions;
    }
    
}