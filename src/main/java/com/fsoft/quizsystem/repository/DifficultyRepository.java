package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyRepository extends JpaRepository<Difficulty, Long> {
}
