package com.learning.quizapp.service;

import static com.learning.quizapp.data.QuestionMockData.getMockQuestions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.learning.quizapp.dao.QuestionRepository;
import com.learning.quizapp.model.Question;

public class QuestionServiceImplTests {

    @InjectMocks
    private QuestionServiceImpl _questionService;

    @Mock
    private QuestionRepository _questionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllQuestions_successful() {
        var mockQuestions = getMockQuestions();
        when(_questionRepository.findAll()).thenReturn(mockQuestions);
        var response = _questionService.getAllQuestions();
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var allQuestions = response.getBody();
        assertTrue(allQuestions instanceof List<Question>);
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(mockQuestions.size(), allQuestions.size());
        assertEquals(mockQuestions.get(0).getQuestionTitle(), allQuestions.get(0).getQuestionTitle());
    }

    @Test
    public void getAllQuestions_throwsException() {
        when(_questionRepository.findAll()).thenThrow(RuntimeException.class);
        var response = _questionService.getAllQuestions();
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var questions = response.getBody();
        assertTrue(questions instanceof List<Question>);
        assertEquals(0,questions.size());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
    }

    @Test
    public void getQuestionsByCategory_successful() {
        var mockQuestions = getMockQuestions();
        when(_questionRepository.findByCategory(anyString())).thenReturn(mockQuestions);
        var response = _questionService.getQuestionsByCategory(anyString());
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var questions = response.getBody();
        assertTrue(questions instanceof List<Question>);
        assertEquals(mockQuestions.size(), questions.size());
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(mockQuestions.get(0).getQuestionTitle(), questions.get(0).getQuestionTitle());
    }

    @Test
    public void getQuestionsByCategory_throwsException() {
        when(_questionRepository.findByCategory(anyString())).thenThrow(RuntimeException.class);
        var response = _questionService.getQuestionsByCategory(anyString());
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var questions = response.getBody();
        assertTrue(questions instanceof List<Question>);
        assertEquals(0,questions.size());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
    }

    @Test
    public void addQuestion_successful() {
        var mockQuestion = getMockQuestions().get(0);
        when(_questionRepository.save(mockQuestion)).thenReturn(mockQuestion);
        var response = _questionService.addQuestion(mockQuestion);
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var questionId = response.getBody();
        assertTrue(questionId instanceof String);
        assertEquals(mockQuestion.getId(), Integer.parseInt(questionId));
        assertEquals(HttpStatus.CREATED, statusCode);
    }

    @Test
    public void addQuestion_throwsException() {
        var mockQuestion = getMockQuestions().get(0);
        when(_questionRepository.save(mockQuestion)).thenThrow(RuntimeException.class);
        var response = _questionService.addQuestion(mockQuestion);
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
    }

    @Test
    public void deleteQuestion_successful() {
        doNothing().when(_questionRepository).deleteById(anyInt());
        var response = _questionService.deleteQuestion(anyInt());
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var status = response.getBody();
        assertTrue(status instanceof Boolean);
        assertTrue(status);
        assertEquals(HttpStatus.NO_CONTENT, statusCode);
        verify(_questionRepository).deleteById(anyInt());
    }

    @Test
    public void deleteQuestion_throwsException() {
        doThrow(RuntimeException.class).when(_questionRepository).deleteById(anyInt());
        var response = _questionService.deleteQuestion(anyInt());
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        verify(_questionRepository).deleteById(anyInt());
    }

    @Test
    public void getQuestionById_successful() {
        var mockQuestion = getMockQuestions().get(0);
        when(_questionRepository.findById(anyInt())).thenReturn(Optional.of(mockQuestion));
        var response = _questionService.getQuestionById(anyInt());
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        var question = response.getBody();
        assertTrue(question instanceof Question);
        assertEquals(mockQuestion.getId(), question.getId());
        assertEquals(HttpStatus.OK, statusCode);
    }

    @Test
    public void getQuestionById_notFound() {
        when(_questionRepository.findById(anyInt())).thenReturn(Optional.empty());
        var response = _questionService.getQuestionById(anyInt());
        assertNotNull(response);
        var statusCode = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, statusCode);
    }
}
