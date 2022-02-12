package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.constant.QuizStatus;
import com.fsoft.quizsystem.object.dto.filter.QuizFilter;
import com.fsoft.quizsystem.object.dto.mapper.QuizMapper;
import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.dto.response.AuthenticationInfo;
import com.fsoft.quizsystem.object.entity.*;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.object.exception.UnauthorizedException;
import com.fsoft.quizsystem.object.validation.RoleValidator;
import com.fsoft.quizsystem.repository.QuizRepository;
import com.fsoft.quizsystem.repository.spec.QuizSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    private final CategoryService categoryService;
    private final UserService userService;
    private final TagService tagService;
    private final DifficultyService difficultyService;
    private final JwtService tokenService;
    private final CloudinaryService cloudinaryService;

    @PostConstruct
    private void init() {
        if (quizRepository.count() == 0) {
            Quiz initialQuiz = new Quiz();
            initialQuiz.setTitle("Elementary HTML/CSS for beginner");
            initialQuiz.setStatus(QuizStatus.PUBLIC);
            initialQuiz.setDescription("Basic HTML/CSS for...");
            initialQuiz.setCategory(categoryService.findCategoryByName("Development"));
            initialQuiz.setInstructor(userService.findUserByUsername("instructor1"));
            initialQuiz.setImage(
                    "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644700398/html-css"
                            + "-course_n8stxa.jpg");

            Tag htmlCssTag = tagService.findTagByName("HTML5/CSS3");

            Question question1 = new Question();
            question1.setTag(htmlCssTag);
            question1.setDifficulty(difficultyService.findDifficultyByLevel(DifficultyLevel.EASY));
            question1.setIsMultiple(false);
            question1.setTitle("What does CSS stand for?");
            List<Answer> answersForQuestion1 = Arrays.asList(
                    new Answer("Creative Style Sheet", false),
                    new Answer("Cascading Style Sheet", true),
                    new Answer("Colorful Style Sheet", false),
                    new Answer("Computer Style Sheet", false)
            );
            answersForQuestion1.forEach(question1::addAnswer);

            Question question2 = new Question();
            question2.setTag(htmlCssTag);
            question2.setDifficulty(difficultyService.findDifficultyByLevel(DifficultyLevel.EASY));
            question2.setIsMultiple(false);
            question2.setTitle("Which CSS property is used to change the text color of an element?");
            List<Answer> answersForQuestion2 = Arrays.asList(
                    new Answer("color", true),
                    new Answer("text-color", false),
                    new Answer("fg-color", false)
            );
            answersForQuestion2.forEach(question1::addAnswer);

            List<Question> initialQuestions = Arrays.asList(question1, question2);
            initialQuestions.forEach(initialQuiz::addQuestion);

            quizRepository.save(initialQuiz);
        }
    }

    public Page<Quiz> findAllQuizzes(QuizFilter filter) {
        Specification<Quiz> specification = QuizSpecification.getSpecification(filter);

        return quizRepository.findAll(specification, filter.getPagination().getPageAndSort());
    }

    public Quiz findQuizById(long id) {
        return quizRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any quiz with id " + id));
    }

    public Quiz findQuizByTitle(String title) {
        return quizRepository.findByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("Not found any quiz with title " + title));
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
