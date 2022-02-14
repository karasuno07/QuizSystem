package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.filter.QuestionFilter;
import com.fsoft.quizsystem.object.dto.mapper.QuestionMapper;
import com.fsoft.quizsystem.object.dto.request.QuestionRequest;
import com.fsoft.quizsystem.object.dto.response.QuestionResponse;
import com.fsoft.quizsystem.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController implements SecuredBearerTokenController {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @PostMapping(value = "/{quizId}/all")
    ResponseEntity<?> getQuestionsByQuizIdWithFilter(@PathVariable Long quizId,
                                           @RequestBody Optional<QuestionFilter> filter) {
        Page<QuestionResponse> responses =
                questionService.findAllQuestionsByQuizId(quizId, filter.orElse(new QuestionFilter()))
                               .map(questionMapper::entityToQuestionResponse);

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @PostMapping
    ResponseEntity<?> addQuestionToQuiz(@RequestBody @Valid QuestionRequest requestBody) {
        QuestionResponse response =
                questionMapper.entityToQuestionResponse(questionService.createQuestion(requestBody));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updateQuestionInQuiz(@PathVariable Long id,
                                           @RequestBody @Valid QuestionRequest requestBody) {
        QuestionResponse response =
                questionMapper.entityToQuestionResponse(questionService.updateQuestion(id, requestBody));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> deleteQuestionFromQuiz(@PathVariable Long id) {
        questionService.deleteQuestion(id);

        return ResponseEntity.ok().build();
    }
}
