package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.dto.mapper.DifficultyMapper;
import com.fsoft.quizsystem.object.entity.es.DifficultyES;
import com.fsoft.quizsystem.object.entity.jpa.Difficulty;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.es.DifficultyEsRepository;
import com.fsoft.quizsystem.repository.jpa.DifficultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Log4j2
public class DifficultyService {

    private final DifficultyRepository difficultyRepository;
    private final DifficultyEsRepository difficultyEsRepository;
    private final DifficultyMapper difficultyMapper;

    public List<Difficulty> findAllDifficulties() {
        List<Difficulty> difficulties;
        if (difficultyEsRepository.count() > 0) {
            difficulties =
                    StreamSupport.stream(difficultyEsRepository.findAll().spliterator(), false)
                                 .map(difficultyMapper::esEntityToJpa)
                                 .collect(Collectors.toList());
        } else {
            difficulties = difficultyRepository.findAll();
        }
        return difficulties;
    }

    public Difficulty findDifficultyById(long id) {
        Optional<DifficultyES> optional = difficultyEsRepository.findById(id);

        if (optional.isPresent()) {
            return difficultyMapper.esEntityToJpa(optional.get());
        } else {
            return difficultyRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any difficulty with id " + id));
        }
    }
}
