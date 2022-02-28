package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.RoleES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoleEsRepository extends ElasticsearchRepository<RoleES, Long> {
}
