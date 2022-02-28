package com.fsoft.quizsystem.repository.jpa;

import com.fsoft.quizsystem.object.entity.jpa.UserQuiz;
import com.fsoft.quizsystem.object.entity.jpa.UserQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizRepository extends JpaRepository<UserQuiz, UserQuizId> {
}
