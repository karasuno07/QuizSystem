package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.TagES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TagEsRepository extends ElasticsearchRepository<TagES, Long> {
}
