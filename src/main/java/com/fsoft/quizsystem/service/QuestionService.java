package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.filter.QuestionFilter;
import com.fsoft.quizsystem.object.dto.mapper.AnswerMapper;
import com.fsoft.quizsystem.object.dto.mapper.QuestionMapper;
import com.fsoft.quizsystem.object.dto.request.QuestionRequest;
import com.fsoft.quizsystem.object.entity.*;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.object.exception.UnauthorizedException;
import com.fsoft.quizsystem.object.validation.RoleValidator;
import com.fsoft.quizsystem.repository.QuestionRepository;
import com.fsoft.quizsystem.repository.spec.QuestionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    private final QuizService quizService;
    private final TagService tagService;
    private final DifficultyService difficultyService;
    private final AnswerService answerService;

    public Page<Question> findAllQuestionsByQuizId(long quizId, QuestionFilter filter) {
        Specification<Question> specification = QuestionSpecification.getSpecification(quizId, filter);
        return questionRepository.findAll(specification, filter.getPagination().getPageAndSort());
    }

    public Question findQuestionById(long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any question with id " + id));
    }

    public Question createQuestion(QuestionRequest requestBody) {
        Question question = questionMapper.questionRequestToEntity(requestBody);
        updateRelationProperties(question, requestBody);

        Set<Answer> answers = requestBody.getAnswers().stream()
                                          .map(answerMapper::answerRequestToEntity)
                                          .collect(Collectors.toSet());
        question.setAnswers(answers);

        return questionRepository.save(question);
    }

    public Question updateQuestion(long id, QuestionRequest requestBody) {
        Question question = this.findQuestionById(id);
        questionMapper.updateEntity(question, requestBody);
        updateRelationProperties(question, requestBody);

        Set<Answer> answers = requestBody.getAnswers().stream()
                                         .map(answerMapper::answerRequestToEntity)
                                         .collect(Collectors.toSet());
        question.setAnswers(answers);

        return questionRepository.save(question);
    }

    public void deleteQuestion(long id) {
        Question question = this.findQuestionById(id);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!RoleValidator.isAdmin(currentUser)) {
            boolean hasAuthorization = currentUser.getId().equals(question.getQuiz().getInstructor().getId());
            if (!hasAuthorization) throw new UnauthorizedException();
        }

        questionRepository.delete(question);
    }

    private void updateRelationProperties(Question question, QuestionRequest requestBody) {
        Quiz quiz = quizService.findQuizById(requestBody.getQuizId());
        question.setQuiz(quiz);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!RoleValidator.isAdmin(currentUser)) {
            boolean hasAuthorization = currentUser.getId().equals(quiz.getInstructor().getId());
            if (!hasAuthorization) throw new UnauthorizedException();
        }

        Tag tag = tagService.findTagById(requestBody.getTagId());
        question.setTag(tag);

        Difficulty difficulty = difficultyService.findDifficultyById(requestBody.getDifficultyId());
        question.setDifficulty(difficulty);

        Set<Answer> answers = requestBody.getAnswers().stream()
                                         .map(answerMapper::answerRequestToEntity)
                                         .collect(Collectors.toSet());
        question.setAnswers(answers);
    }
}
