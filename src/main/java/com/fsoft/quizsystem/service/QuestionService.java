package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuestionService {

    private final QuestionRepository questionRepository;
}
