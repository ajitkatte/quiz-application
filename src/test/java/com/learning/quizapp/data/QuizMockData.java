package com.learning.quizapp.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.learning.quizapp.model.Question;
import com.learning.quizapp.model.Quiz;
import com.learning.quizapp.model.QuizQuestion;

public class QuizMockData {
    public static Quiz getMockQuiz(Optional<String> title) {
        var questions = QuestionMockData.getMockQuestions();
        var quizTitle = title.orElse("Test Quiz");
        var quiz = new Quiz();
        quiz.setTitle(quizTitle);
        quiz.setQuestions(questions);
        return quiz;
    }

    public static List<QuizQuestion> getMockQuizQuestions() {
        var questions = QuestionMockData.getMockQuestions();
        QuizQuestion quizQuestion = questions
                .stream()
                .map(q -> mapToQuizQuestion(q))
                .collect(Collectors.toList())
                .get(0);
                
        var quizQuestions = new ArrayList<QuizQuestion>() {
            {
                add(quizQuestion);
            }
        };
        return quizQuestions;
    }

    private static QuizQuestion mapToQuizQuestion(Question question) {
        return new QuizQuestion(question.getId(), question.getQuestionTitle(), question.getOption1(),
                question.getOption2(), question.getOption3(), question.getOption4());
    }
}
