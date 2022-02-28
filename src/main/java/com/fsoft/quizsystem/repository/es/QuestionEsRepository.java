package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.QuestionES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionEsRepository extends ElasticsearchRepository<QuestionES, Long> {
}
