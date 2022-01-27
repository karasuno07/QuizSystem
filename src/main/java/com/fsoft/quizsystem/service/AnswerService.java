package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.entity.Answer;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnswerService {

    private final AnswerRepository answerRepository;

    public List<Answer> findAllAnswersByQuestionId(long questionId) {
        return answerRepository.findAllByQuestionId(questionId);
    }

    public Answer findAnswerById(long id) {
        return answerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any answer with id " + id));
    }
}
