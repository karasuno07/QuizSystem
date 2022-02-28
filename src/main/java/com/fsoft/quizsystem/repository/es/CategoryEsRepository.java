package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.CategoryES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryEsRepository extends ElasticsearchRepository<CategoryES, Long> {
}
