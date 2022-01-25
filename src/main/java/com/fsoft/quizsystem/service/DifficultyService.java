package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.repository.DifficultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class DifficultyService {

    private final DifficultyRepository difficultyRepository;
}
