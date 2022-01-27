package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByQuestionId(long questionId);
}
