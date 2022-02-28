package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.QuizES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuizEsRepository extends ElasticsearchRepository<QuizES, Long> {
}
