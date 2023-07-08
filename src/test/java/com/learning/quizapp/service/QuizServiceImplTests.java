package com.learning.quizapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.learning.quizapp.dao.QuestionRepository;
import com.learning.quizapp.dao.QuizRepository;
import static com.learning.quizapp.data.QuizMockData.*;

import static com.learning.quizapp.data.QuestionMockData.*;

public class QuizServiceImplTests {
    @InjectMocks
    private QuizServiceImpl _quizService;

    @Mock
    private QuizRepository _quizRepository;

    @Mock
    private QuestionRepository _questionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createQuiz_successful() {
        var mockQuestions = getMockQuestions();
        var mockQuiz = getMockQuiz(Optional.empty());
        when(_questionRepository.findRandomQuestionsByCategory(mockQuestions.get(0).getCategory(), 5)).thenReturn(mockQuestions);
        when(_quizRepository.save(mockQuiz)).thenReturn(mockQuiz);

        var response = _quizService.createQuiz(mockQuestions.get(0).getCategory(), 5, mockQuiz.getTitle());

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var createdQuizId = response.getBody();
        assertNotNull(createdQuizId);
        assertTrue(createdQuizId instanceof Integer);
        assertEquals(mockQuiz.getId(), createdQuizId);
    }

    @Test
    public void getQuizQuestions_successful() {
        var mockQuiz = getMockQuiz(Optional.empty());
        when(_quizRepository.findById(anyInt())).thenReturn(Optional.of(mockQuiz));
        var response = _quizService.getQuizQuestions(anyInt());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var quizQuestions = response.getBody();
        assertNotNull(quizQuestions);
        assertEquals(mockQuiz.getQuestions().size(), quizQuestions.size());
        assertEquals(mockQuiz.getQuestions().get(0).getQuestionTitle(), quizQuestions.get(0).questionTitle());
    }

    @Test
    public void getQuizQuestions_notFound() {
        when(_quizRepository.findById(anyInt())).thenReturn(Optional.empty());
        var response = _quizService.getQuizQuestions(anyInt());
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
