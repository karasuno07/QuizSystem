package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.mapper.QuestionMapper;
import com.fsoft.quizsystem.object.dto.request.QuestionRequest;
import com.fsoft.quizsystem.object.dto.response.QuestionResponse;
import com.fsoft.quizsystem.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController implements SecuredBearerTokenController {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @GetMapping(value = "/{quizId}")
    ResponseEntity<?> getQuestionsByQuizId(@PathVariable Long quizId) {
        List<QuestionResponse> responses = questionService.findAllQuestionsByQuizId(quizId).stream()
                                                          .map(questionMapper::entityToQuestionResponse)
                                                          .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping
    ResponseEntity<?> addQuestionToQuiz(@RequestBody @Valid QuestionRequest requestBody) {
        QuestionResponse response =
                questionMapper.entityToQuestionResponse(questionService.createQuestion(requestBody));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updateQuestionInQuiz(@PathVariable Long id,
                                           @RequestBody @Valid QuestionRequest requestBody) {
        QuestionResponse response =
                questionMapper.entityToQuestionResponse(questionService.updateQuestion(id, requestBody));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> deleteQuestionFromQuiz(@PathVariable Long id) {
        questionService.deleteQuestion(id);

        return ResponseEntity.ok().build();
    }
}
