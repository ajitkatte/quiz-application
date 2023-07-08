package com.learning.quizapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.quizapp.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

}
