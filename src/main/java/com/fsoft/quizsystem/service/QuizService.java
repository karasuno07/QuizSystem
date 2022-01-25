package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.constant.QuizStatus;
import com.fsoft.quizsystem.object.dto.filter.QuizFilter;
import com.fsoft.quizsystem.object.dto.mapper.QuizMapper;
import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.entity.Category;
import com.fsoft.quizsystem.object.entity.Quiz;
import com.fsoft.quizsystem.object.entity.User;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.*;
import com.fsoft.quizsystem.repository.spec.QuizSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    private final CategoryService categoryService;

    public Page<Quiz> findAllQuizzes(QuizFilter filter) {
        Specification<Quiz> specification = QuizSpecification.getSpecification(filter);

        return quizRepository.findAll(specification, filter.getPagination().getPageAndSort());
    }

    public Quiz findQuizById(long id) {
        return quizRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any quiz with id " + id));
    }

    public Quiz createQuiz(QuizRequest requestBody) {
        Quiz quiz = quizMapper.quizRequestToEntity(requestBody);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        quiz.setInstructor(currentUser);

        Category category = categoryService.findCategoryById(requestBody.getCategoryId());
        quiz.setCategory(category);

        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(long id, QuizRequest requestBody) {
        Quiz quiz = this.findQuizById(id);
        quizMapper.updateEntity(quiz, requestBody);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        quiz.setInstructor(currentUser);

        Category category = categoryService.findCategoryById(requestBody.getCategoryId());
        quiz.setCategory(category);

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(long id) {
        Quiz quiz = this.findQuizById(id);

        quizRepository.delete(quiz);
    }

    public void changeQuizStatus(long id, String status) {
        Quiz quiz = this.findQuizById(id);
        quiz.setStatus(Enum.valueOf(QuizStatus.class, status));

        quizRepository.delete(quiz);
    }

}
