package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.mapper.DifficultyMapper;
import com.fsoft.quizsystem.object.dto.response.DifficultyResponse;
import com.fsoft.quizsystem.service.DifficultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/difficulties")
@RequiredArgsConstructor
public class DifficultyController {

    private final DifficultyService difficultyService;
    private final DifficultyMapper difficultyMapper;

    @GetMapping
    ResponseEntity<?> getAllDifficulties() {
        List<DifficultyResponse> responses = difficultyService.findAllDifficulties().stream()
                                                              .map(difficultyMapper::entityToDifficultyResponse)
                                                              .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getDifficultyById(@PathVariable Long id) {
        DifficultyResponse response =
                difficultyMapper.entityToDifficultyResponse(difficultyService.findDifficultyById(id));
        return ResponseEntity.ok(response);
    }
}
