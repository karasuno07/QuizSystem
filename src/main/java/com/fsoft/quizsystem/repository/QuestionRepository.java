package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByQuizId(long quizId);
}
