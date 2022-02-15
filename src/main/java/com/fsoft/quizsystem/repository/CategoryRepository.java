package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);
}
