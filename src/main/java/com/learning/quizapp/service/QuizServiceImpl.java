package com.learning.quizapp.service;

import java.util.*;
import java.util.stream.*;

import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.learning.quizapp.dao.*;
import com.learning.quizapp.model.*;

@Service
public class QuizServiceImpl implements QuizService {

    private QuizRepository _quizRepository;
    private QuestionRepository _questionRepository;

    public QuizServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this._quizRepository = quizRepository;
        this._questionRepository = questionRepository;
    }

    public ResponseEntity<Integer> createQuiz(String category, int numQ, String title) {

        var questions = _questionRepository.findRandomQuestionsByCategory(category, numQ);

        var quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        var createdQuiz = _quizRepository.save(quiz);

        return new ResponseEntity<>(createdQuiz.getId(), HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(Integer id) {
        var quiz = _quizRepository.findById(id);

        if (quiz.isPresent()) {
            List<Question> questions = quiz.get().getQuestions();

            var quizQuestions = questions
                    .stream()
                    .map(q -> mapToQuizQuestion(q))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(quizQuestions, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Integer> calculateSubmissionScore(Integer quizId, List<QuizSubmission> submissions) {
        var quiz = _quizRepository.findById(quizId);

        if (quiz.isPresent()) {
            var questions = quiz.get().getQuestions();
            int score = (int) submissions
                    .stream()
                    .filter(s -> s.answer().equalsIgnoreCase(getRightAnswer(questions, s.questionId())))
                    .count();

            return new ResponseEntity<>(score, HttpStatus.OK);
        }

        return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<QuizDto> getQuizById(Integer quizId) {
        var quiz = _quizRepository.findById(quizId);
        if (quiz.isPresent()) {
            List<Question> questions = quiz.get().getQuestions();
            var quizQuestions = questions
                    .stream()
                    .map(q -> mapToQuizQuestion(q))
                    .collect(Collectors.toList());
            var quizDto = new QuizDto(quizId, quiz.get().getTitle(), quizQuestions);
            return new ResponseEntity<>(quizDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> deleteQuiz(Integer id) {
        try {
            _quizRepository.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<QuizDto>> getAll() {
        List<Quiz> quizzes = _quizRepository.findAll();
        var quizzesDto = quizzes
                .stream()
                .map(qz -> mapToQuizDto(qz))
                .collect(Collectors.toList());
        return new ResponseEntity<>(quizzesDto, HttpStatus.OK);
    }

    public ResponseEntity<QuizDto> updateQuizTitle(Integer id, String title) {
        try {
            var quiz = _quizRepository.findById(id);
            if (quiz.isPresent()) {
                quiz.get().setTitle(title);
                var updatedQuiz = _quizRepository.save(quiz.get());
                var quizDto = mapToQuizDto(updatedQuiz);
                return new ResponseEntity<>(quizDto, HttpStatus.ACCEPTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    private QuizDto mapToQuizDto(Quiz quiz) {
        List<QuizQuestion> quizQuestionDto = quiz
                .getQuestions()
                .stream()
                .map(q -> mapToQuizQuestion(q))
                .collect(Collectors.toList());

        return new QuizDto(quiz.getId(), quiz.getTitle(), quizQuestionDto);
    }

    private QuizQuestion mapToQuizQuestion(Question q) {
        return new QuizQuestion(q.getId(),
                q.getQuestionTitle(),
                q.getOption1(),
                q.getOption2(),
                q.getOption3(),
                q.getOption4());
    }

    private String getRightAnswer(List<Question> questions, Integer id) {
        Optional<Question> question = questions
                .stream()
                .filter(q -> q.getId() == id)
                .findFirst();

        return question.isPresent()
                ? question.get().getAnswer()
                : null;
    }
}
