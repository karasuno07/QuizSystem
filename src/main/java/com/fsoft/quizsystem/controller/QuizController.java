package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.filter.QuizFilter;
import com.fsoft.quizsystem.object.dto.mapper.QuizMapper;
import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.dto.response.QuizResponse;
import com.fsoft.quizsystem.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController implements SecuredBearerTokenController {

    private final QuizService quizService;
    private final QuizMapper quizMapper;

    @PostMapping(value = "/find-all")
    ResponseEntity<?> getAllQuizzesWithFilter(@RequestBody Optional<QuizFilter> filter) {
        Page<QuizResponse> responses = quizService.findAllQuizzes(filter.orElse(new QuizFilter()))
                                                  .map(quizMapper::entityToQuizResponse);

        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getQuizById(@PathVariable("id") Long id) {
        QuizResponse response = quizMapper.entityToQuizResponse(quizService.findQuizById(id));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping
    ResponseEntity<?> createQuiz(@ModelAttribute @Valid QuizRequest body) {
        QuizResponse response = quizMapper.entityToQuizResponse(quizService.createQuiz(body));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")// TODO: modify precision authority
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updateQuiz(@PathVariable("id") Long id, @ModelAttribute @Valid QuizRequest body) {
        QuizResponse response = quizMapper.entityToQuizResponse(quizService.updateQuiz(id, body));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')") // TODO: modify precision authority
    @PatchMapping(value = "/{id}/{status}")
    ResponseEntity<?> changeQuizStatus(@PathVariable("id") Long id, @PathVariable("status") String status) {
        quizService.changeQuizStatus(id, status);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> deleteQuiz(@PathVariable("id") Long id) {
        quizService.deleteQuiz(id);

        return ResponseEntity.ok().build();
    }
}
