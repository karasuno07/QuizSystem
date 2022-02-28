package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.AnswerES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnswerEsRepository extends ElasticsearchRepository<AnswerES, Long> {
}
