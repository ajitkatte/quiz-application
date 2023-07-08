package com.learning.quizapp.controller;

import static com.learning.quizapp.data.QuestionMockData.getMockQuestions;
import static com.learning.quizapp.util.TestUtil.getOkResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.learning.quizapp.controller.QuestionController;
import com.learning.quizapp.model.Question;
import com.learning.quizapp.service.QuestionService;

public class QuestionControllerTests {
    
    @InjectMocks
    private QuestionController _questionController;

    @Mock
    private QuestionService _questionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllQuestions_successful() {
        List<Question> mockQuestions = getMockQuestions();        
        when(_questionService.getAllQuestions()).thenReturn(getOkResponse(mockQuestions));
        var response = _questionController.getAllQuestions();
        assertNotNull(response);
        var questions = response.getBody();
        assertNotNull(questions);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(questions instanceof List<Question>);
        assertEquals(1, questions.size());
        assertEquals("Java", questions.get(0).getCategory());
    }

    @Test
    void getAllQuestions_Throws_Exception() {    
        when(_questionService.getAllQuestions()).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> _questionController.getAllQuestions());
    }

    @Test
    void getQuestionsByCategory_successful(){
        var mockQuestions = getMockQuestions();
        when(_questionService.getQuestionsByCategory(anyString())).thenReturn(getOkResponse(mockQuestions));
        var response = _questionController.getQuestionsByCategory(anyString());
        assertNotNull(response);
        var questions = response.getBody();
        assertNotNull(questions);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(questions instanceof List<Question>);
        assertEquals(1, questions.size());
        assertEquals("Java", questions.get(0).getCategory());
    }

    @Test
    void getQuestionsByCategory_Throws_Exception() {    
        when(_questionService.getQuestionsByCategory(anyString())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> _questionController.getQuestionsByCategory(anyString()));
    }

    @Test
    void getQuestionById_successful() {
        var mockQuestions = getMockQuestions();
        when(_questionService.getQuestionById(anyInt())).thenReturn(getOkResponse(mockQuestions.get(0)));
        var response = _questionController.getQuestionById(anyInt());
        assertNotNull(response);
        var question = response.getBody();
        assertNotNull(question);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(question instanceof Question);
        assertEquals("Java", question.getCategory());
        assertEquals(mockQuestions.get(0).getQuestionTitle(), question.getQuestionTitle());
    }

    @Test
    void addQuestion_successful() {
        String mockQuestionId = "1";
        when(_questionService.addQuestion(any(Question.class))).thenReturn(getOkResponse(mockQuestionId));
        var response = _questionController.addQuestion(mock(Question.class));
        assertNotNull(response);
        var questionId = response.getBody();
        assertNotNull(questionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(questionId instanceof String);
        assertEquals(mockQuestionId, questionId);
    }

    @Test
    void deleteQuestion_successful(){
        when(_questionService.deleteQuestion(anyInt())).thenReturn(getOkResponse(true));
        var response = _questionController.deleteQuestion(anyInt());
        assertNotNull(response);
        var isQuestionDeleted = response.getBody();
        assertNotNull(isQuestionDeleted);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(isQuestionDeleted instanceof Boolean);
        assertEquals(true, isQuestionDeleted);
    }
}
