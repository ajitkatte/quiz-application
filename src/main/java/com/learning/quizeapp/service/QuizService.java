package com.learning.quizeapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.learning.quizeapp.dao.QuestionDao;
import com.learning.quizeapp.dao.QuizDao;
import com.learning.quizeapp.model.Question;
import com.learning.quizeapp.model.Quiz;
import com.learning.quizeapp.model.QuizQuestion;
import com.learning.quizeapp.model.QuizSubmission;

@Service
public class QuizService {

    @Autowired
    private QuizDao _quizDao;
    @Autowired
    private QuestionDao _questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        var questions = _questionDao.findRandomQuestionsByCategory(category, numQ);

        var quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        var createdQuiz = _quizDao.save(quiz);

        return new ResponseEntity<String>(Integer.toString(createdQuiz.getId()), HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(Integer id) {
        var quiz = _quizDao.findById(id);

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

    private QuizQuestion mapToQuizQuestion(Question q) {
        return new QuizQuestion(q.getId(),
                q.getQuestionTitle(),
                q.getOption1(),
                q.getOption2(),
                q.getOption3(),
                q.getOption4());
    }

    public ResponseEntity<Integer> calculateSubmissionScore(Integer quizId, List<QuizSubmission> submissions) {
        var quiz = _quizDao.findById(quizId);
        
        if (quiz.isPresent()) {
            var questions = quiz.get().getQuestions();
            int score = (int) submissions
                    .stream()
                    .filter(s -> s.getAnswer().equalsIgnoreCase(getRightAnswer(questions, s.getQuestionId())))
                    .count();

            return new ResponseEntity<Integer>(score, HttpStatus.OK);
        }
        
        return new ResponseEntity<Integer>(0, HttpStatus.NOT_FOUND);
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
