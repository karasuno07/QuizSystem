package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.filter.QuizFilter;
import com.fsoft.quizsystem.object.dto.mapper.QuizMapper;
import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.dto.response.QuizResponse;
import com.fsoft.quizsystem.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizMapper quizMapper;

    @GetMapping(consumes = "application/json")
    ResponseEntity<?> getAllQuizzes(@RequestBody Optional<QuizFilter> filter) {
        List<QuizResponse> responses = quizService.findAllQuizzes(filter.orElse(new QuizFilter())).stream()
                                                  .map(quizMapper::entityToQuizResponse)
                                                  .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    //    @PreAuthorize("isAuthenticated() AND authentication.principal.id == #id OR hasAuthority
    //    ('QUIZ_READ')")
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getQuizById(@PathVariable("id") Long id) {
        QuizResponse response = quizMapper.entityToQuizResponse(quizService.findQuizById(id));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('QUIZ_CREATE')")
    @PostMapping
    ResponseEntity<?> createQuiz(@ModelAttribute @Valid QuizRequest body) {
        QuizResponse response = quizMapper.entityToQuizResponse(quizService.createQuiz(body));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('QUIZ_UPDATE')") // TODO: modify precision authority
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updateQuiz(@PathVariable("id") Long id, @ModelAttribute @Valid QuizRequest body) {
        QuizResponse response = quizMapper.entityToQuizResponse(quizService.updateQuiz(id, body));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('QUIZ_UPDATE')") // TODO: modify precision authority
    @PatchMapping(value = "/{id}/{status}")
    ResponseEntity<?> changeQuizStatus(@PathVariable("id") Long id, @PathVariable("status") String status) {
        quizService.changeQuizStatus(id, status);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('QUIZ_DELETE')")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> deleteQuiz(@PathVariable("id") Long id) {
        quizService.deleteQuiz(id);

        return ResponseEntity.ok().build();
    }
}
