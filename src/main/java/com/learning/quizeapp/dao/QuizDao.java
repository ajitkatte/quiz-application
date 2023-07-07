package com.learning.quizeapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.quizeapp.model.Quiz;

public interface QuizDao extends JpaRepository<Quiz, Integer> {

}
