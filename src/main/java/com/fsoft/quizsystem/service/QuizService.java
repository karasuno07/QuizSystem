package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.constant.QuizStatus;
import com.fsoft.quizsystem.object.dto.filter.QuizFilter;
import com.fsoft.quizsystem.object.dto.mapper.QuizMapper;
import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.dto.response.AuthenticationInfo;
import com.fsoft.quizsystem.object.entity.es.QuizES;
import com.fsoft.quizsystem.object.entity.jpa.Category;
import com.fsoft.quizsystem.object.entity.jpa.Quiz;
import com.fsoft.quizsystem.object.entity.jpa.User;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.object.exception.UnauthorizedException;
import com.fsoft.quizsystem.object.validation.RoleValidator;
import com.fsoft.quizsystem.repository.es.QuizEsRepository;
import com.fsoft.quizsystem.repository.jpa.QuizRepository;
import com.fsoft.quizsystem.repository.jpa.spec.QuizSpecification;
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

@Service
@RequiredArgsConstructor
@Log4j2
public class QuizService {

    private final ElasticsearchOperations restTemplate;

    private final QuizRepository quizRepository;
    private final QuizEsRepository quizEsRepository;
    private final QuizMapper quizMapper;

    private final CategoryService categoryService;
    private final JwtService tokenService;
    private final CloudinaryService cloudinaryService;

    public Page<Quiz> findAllQuizzes(QuizFilter filter) {
        Page<Quiz> quizzes;

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (!ObjectUtils.isEmpty(filter.getTitle())) {
            query.should(QueryBuilders.matchQuery("title", filter.getTitle()));
        }
        if (!ObjectUtils.isEmpty(filter.getCategoryName())) {
            query.should(QueryBuilders.matchQuery("category.name", filter.getCategoryName()));
        }
        if (!ObjectUtils.isEmpty(filter.getStatus())) {
            query.should(QueryBuilders.termQuery("status", filter.getStatus()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(filter.getPagination().getPageAndSort())
                .build();
        SearchHits<QuizES> hits = restTemplate.search(searchQuery, QuizES.class,
                                                      IndexCoordinates.of("quizzes"));
        if (hits.hasSearchHits()) {
            SearchPage<QuizES> page = SearchHitSupport.searchPageFor(hits, filter.getPagination()
                                                                                 .getPageAndSort());
            quizzes = page.map(SearchHit::getContent).map(quizMapper::esEntityToJpa);
        } else {
            Specification<Quiz> specification = QuizSpecification.getSpecification(filter);
            quizzes = quizRepository.findAll(specification, filter.getPagination().getPageAndSort());
        }

        return quizzes;
    }

    public Quiz findQuizById(long id) {
        Optional<QuizES> optional = quizEsRepository.findById(id);

        if (optional.isPresent()) {
            return quizMapper.esEntityToJpa(optional.get());
        } else {
            return quizRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any quiz with id " + id));
        }
    }

    public Quiz createQuiz(QuizRequest requestBody) {
        Quiz quiz = quizMapper.quizRequestToEntity(requestBody);

        AuthenticationInfo currentUser = (AuthenticationInfo) SecurityContextHolder.getContext()
                                                                                   .getAuthentication()
                                                                                   .getPrincipal();
        User instructor = tokenService.getUserById(currentUser.getId());
        quiz.setInstructor(instructor);

        Category category = categoryService.findCategoryById(requestBody.getCategoryId());
        quiz.setCategory(category);

        if (!ObjectUtils.isEmpty(requestBody.getImageFile())) {
            String image = cloudinaryService.uploadImage(null, requestBody.getImageFile());
            if (image != null) quiz.setImage(image);
        }

        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(long id, QuizRequest requestBody) {
        Quiz quiz = this.findQuizById(id);
        quizMapper.updateEntity(quiz, requestBody);

        AuthenticationInfo currentUser = (AuthenticationInfo) SecurityContextHolder.getContext()
                                                                                   .getAuthentication()
                                                                                   .getPrincipal();
        User instructor = tokenService.getUserById(currentUser.getId());
        quiz.setInstructor(instructor);
        if (!RoleValidator.isAdmin(instructor)) {
            boolean hasAuthorization = instructor.getId().equals(quiz.getInstructor().getId());
            if (!hasAuthorization) throw new UnauthorizedException();
        }

        Category category = categoryService.findCategoryById(requestBody.getCategoryId());
        quiz.setCategory(category);

        if (!ObjectUtils.isEmpty(requestBody.getImageFile())) {
            String image = cloudinaryService.uploadImage(quiz.getImage(), requestBody.getImageFile());
            if (image != null) quiz.setImage(image);
        }

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(long id) {
        Quiz quiz = this.findQuizById(id);

        AuthenticationInfo currentUser = (AuthenticationInfo) SecurityContextHolder.getContext()
                                                                                   .getAuthentication()
                                                                                   .getPrincipal();
        User instructor = tokenService.getUserById(currentUser.getId());
        if (!RoleValidator.isAdmin(instructor)) {
            boolean hasAuthorization = instructor.getId().equals(quiz.getInstructor().getId());
            if (!hasAuthorization) throw new UnauthorizedException();
        }

        quizRepository.delete(quiz);
    }

    public void changeQuizStatus(long id, String status) {
        Quiz quiz = this.findQuizById(id);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!RoleValidator.isAdmin(currentUser)) {
            boolean hasAuthorization = currentUser.getId().equals(quiz.getInstructor().getId());
            if (!hasAuthorization) throw new UnauthorizedException();
        }

        quiz.setStatus(Enum.valueOf(QuizStatus.class, status));

        quizRepository.delete(quiz);
    }

}
