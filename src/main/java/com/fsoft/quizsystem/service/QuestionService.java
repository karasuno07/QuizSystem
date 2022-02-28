package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.filter.QuestionFilter;
import com.fsoft.quizsystem.object.dto.mapper.AnswerMapper;
import com.fsoft.quizsystem.object.dto.mapper.QuestionMapper;
import com.fsoft.quizsystem.object.dto.request.QuestionRequest;
import com.fsoft.quizsystem.object.entity.es.QuestionES;
import com.fsoft.quizsystem.object.entity.jpa.*;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.object.exception.UnauthorizedException;
import com.fsoft.quizsystem.object.validation.RoleValidator;
import com.fsoft.quizsystem.repository.es.QuestionEsRepository;
import com.fsoft.quizsystem.repository.jpa.QuestionRepository;
import com.fsoft.quizsystem.repository.jpa.spec.QuestionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuestionService {

    private final ElasticsearchOperations restTemplate;

    private final QuestionRepository questionRepository;
    private final QuestionEsRepository questionEsRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    private final QuizService quizService;
    private final TagService tagService;
    private final DifficultyService difficultyService;

    public Page<Question> findAllQuestionsByQuizId(long quizId, QuestionFilter filter) {
        Page<Question> questions;

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.should(QueryBuilders.termQuery("quiz", quizId));
        if (!ObjectUtils.isEmpty(filter.getDifficultyId())) {
            query.should(QueryBuilders.termQuery("difficulty", filter.getDifficultyId()));
        }
        if (!ObjectUtils.isEmpty(filter.getTagId())) {
            query.should(QueryBuilders.termQuery("tag", filter.getTagId()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(filter.getPagination().getPageAndSort())
                .build();
        SearchHits<QuestionES> hits = restTemplate.search(searchQuery, QuestionES.class,
                                                          IndexCoordinates.of("questions"));

        if (hits.hasSearchHits()) {
            SearchPage<QuestionES> page = SearchHitSupport.searchPageFor(hits, filter.getPagination()
                                                                                     .getPageAndSort());
            questions = page.map(SearchHit::getContent)
                            .map(questionMapper::esEntityToJpa);
        } else {
            Specification<Question> specification = QuestionSpecification.getSpecification(quizId, filter);
            questions = questionRepository.findAll(specification, filter.getPagination().getPageAndSort());
        }

        return questions;
    }

    public Question findQuestionById(long id) {
        Optional<QuestionES> optional = questionEsRepository.findById(id);

        if (optional.isPresent()) {
            return questionMapper.esEntityToJpa(optional.get());
        } else {
            return questionRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any question with id " + id));
        }
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
