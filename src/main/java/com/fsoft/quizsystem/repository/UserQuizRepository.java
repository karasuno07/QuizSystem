package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.UserQuiz;
import com.fsoft.quizsystem.object.entity.UserQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizRepository extends JpaRepository<UserQuiz, UserQuizId> {
}
