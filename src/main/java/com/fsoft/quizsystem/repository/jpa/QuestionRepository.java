package com.fsoft.quizsystem.repository.jpa;

import com.fsoft.quizsystem.object.entity.jpa.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionRepository extends JpaRepository<Question, Long>,
        JpaSpecificationExecutor<Question> {
}
