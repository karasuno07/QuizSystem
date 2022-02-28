package com.fsoft.quizsystem.repository.es;

import com.fsoft.quizsystem.object.entity.es.UserES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserEsRepository extends ElasticsearchRepository<UserES, Long> {

    Optional<UserES> findByUsername(String username);

    Optional<UserES> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
