package com.learning.quizeapp.controller;

import static com.learning.quizeapp.util.TestUtil.getOkResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.learning.quizeapp.model.QuizDto;
import com.learning.quizeapp.model.QuizQuestion;
import com.learning.quizeapp.model.QuizSubmission;
import com.learning.quizeapp.service.QuizService;

public class QuizControllerTests {
    @InjectMocks
    private QuizController _quizController;

    @Mock
    private QuizService _quizService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createQuiz_successful() {
        when(_quizService.createQuiz(anyString(), anyInt(), anyString())).thenReturn(getOkResponse(1));
        var response = _quizController.createQuiz(anyString(), anyInt(), anyString());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var quizId = response.getBody();
        assertEquals(1, quizId);
    }

    @Test
    public void getQuizQuestions_successful() {
        List<QuizQuestion> mockQuizQuestions = getMockQuizQuestions();
        when(_quizService.getQuizQuestions(anyInt())).thenReturn(getOkResponse(mockQuizQuestions));
        var response = _quizController.getQuizQuestions(anyInt());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var quizQuestions = response.getBody();
        assertNotNull(quizQuestions);
        assertTrue(quizQuestions instanceof List<QuizQuestion>);
        assertEquals(1, quizQuestions.size());
        assertEquals(mockQuizQuestions.get(0).questionTitle(), quizQuestions.get(0).questionTitle());
    }

    @Test
    public void getQuizById_successful() {
        QuizDto mockQuizDto = getMockQuizDto(Optional.empty());
        when(_quizService.getQuizById(anyInt())).thenReturn(getOkResponse(mockQuizDto));
        var response = _quizController.getQuizById(anyInt());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var quizDto = response.getBody();
        assertNotNull(quizDto);
        assertTrue(quizDto instanceof QuizDto);
        assertEquals(mockQuizDto.title(), quizDto.title());
        assertEquals(mockQuizDto.questions().size(), quizDto.questions().size());
    }

    @Test
    public void getAll_successful() {
        List<QuizDto> mockQiuzzes = getMockQuizzes();
        when(_quizService.getAll()).thenReturn(getOkResponse(mockQiuzzes));
        var response = _quizController.getAll();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var quizzes = response.getBody();
        assertNotNull(quizzes);
        assertTrue(quizzes instanceof List<QuizDto>);
        assertEquals(mockQiuzzes.size(), quizzes.size());
        assertEquals(mockQiuzzes.get(0).title(), quizzes.get(0).title());
        assertEquals(mockQiuzzes.get(0).questions().size(), quizzes.get(0).questions().size());
    }

    @Test
    public void deleteQuiz_successful() {
        when(_quizService.deleteQuiz(anyInt())).thenReturn(getOkResponse(true));
        var response = _quizController.deleteQuiz(anyInt());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var isQuizDeleted = response.getBody();
        assertNotNull(isQuizDeleted);
        assertTrue(isQuizDeleted instanceof Boolean);
        assertEquals(true, isQuizDeleted);
    }

    @Test
    public void updateQuizTitle_successful() {
        var mockQuiz = getMockQuiz("mock quiz");
        when(_quizService.updateQuizTitle(anyInt(), anyString())).thenReturn(getOkResponse(mockQuiz));
        var response = _quizController.updateQuizTitle(anyInt(), anyString());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        QuizDto quiz = response.getBody();
        assertNotNull(quiz);
        assertTrue(quiz instanceof QuizDto);
        assertEquals(mockQuiz.title(), quiz.title());
    }

    @Test
    public void calculateSubmissionScore_successful() {
        when(_quizService.calculateSubmissionScore(anyInt(), Mockito.<QuizSubmission>anyList())).thenReturn(getOkResponse(3));
        var response = _quizController.submitQuizAndCalculateScore(anyInt(), Mockito.<QuizSubmission>anyList());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer score = response.getBody();
        assertTrue(score instanceof Integer);
        assertEquals(3, score);
    }

    private QuizDto getMockQuiz(String title) {
        var quiz = getMockQuizDto(Optional.of(title));
        return quiz;
    }

    private List<QuizDto> getMockQuizzes() {
        var quizzes = new ArrayList<QuizDto>() {
            {
                add(getMockQuizDto(Optional.empty()));
            }
        };
        return quizzes;
    }

    private List<QuizQuestion> getMockQuizQuestions() {
        var quizQuestion = new QuizQuestion(1, "Java Quiz", "1", "2", "3", "4");
        var quizQuestions = new ArrayList<QuizQuestion>() {
            {
                add(quizQuestion);
            }
        };
        return quizQuestions;
    }

    private QuizDto getMockQuizDto(Optional<String> quizTitle) {
        var title = quizTitle.orElse("Java Quiz");
        var quizDto = new QuizDto(1, title, getMockQuizQuestions());
        return quizDto;
    }

}
