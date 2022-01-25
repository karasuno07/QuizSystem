package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
