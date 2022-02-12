package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {

    Optional<Quiz> findByTitle(String title);
}
