package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
