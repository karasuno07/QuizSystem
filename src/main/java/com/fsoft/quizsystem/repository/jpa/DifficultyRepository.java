package com.fsoft.quizsystem.repository.jpa;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.entity.jpa.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DifficultyRepository extends JpaRepository<Difficulty, Long> {

    Optional<Difficulty> findByLevel(DifficultyLevel level);
}
