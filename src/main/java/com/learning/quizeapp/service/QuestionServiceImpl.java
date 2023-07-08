package com.learning.quizeapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.learning.quizeapp.dao.QuestionDao;
import com.learning.quizeapp.model.Question;

@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionDao _questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this._questionDao = questionDao;
    }

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(_questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
       try {
            return new ResponseEntity<>(_questionDao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            Question res = _questionDao.save(question);
            var id = Integer.toString(res.getId());
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.getInternalServerError("Something went wrong while adding a question.");
    }

    public ResponseEntity<Boolean> deleteQuestion(int id) {
        try {
            _questionDao.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.getInternalServerError(null);
    }

    public ResponseEntity<Question> getQuestionById(int id) {
        var question = _questionDao.findById(id);
        return question.isPresent()
                ? new ResponseEntity<>(question.get(), HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private <T> ResponseEntity<T> getInternalServerError(T t) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t);
    }
}
