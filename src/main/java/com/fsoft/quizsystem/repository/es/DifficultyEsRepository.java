package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.DifficultyES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DifficultyEsRepository extends ElasticsearchRepository<DifficultyES, Long> {
}
