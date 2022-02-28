package com.fsoft.quizsystem.repository.jpa;

import com.fsoft.quizsystem.object.entity.jpa.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByQuestionId(long questionId);
}
