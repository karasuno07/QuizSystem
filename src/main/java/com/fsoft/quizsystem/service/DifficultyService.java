package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.constant.DifficultyPoint;
import com.fsoft.quizsystem.object.entity.Difficulty;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.DifficultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DifficultyService {

    private final DifficultyRepository difficultyRepository;

    public List<Difficulty> findAllDifficulties() {
        return difficultyRepository.findAll();
    }

    public Difficulty findDifficultyById(long id) {
        return difficultyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any difficulty with id " + id));
    }

    public Difficulty findDifficultyByLevel(DifficultyLevel level) {
        return difficultyRepository.findByLevel(level).orElseThrow(
                () -> new ResourceNotFoundException("Not found any difficulty with level " + level));
    }
}
