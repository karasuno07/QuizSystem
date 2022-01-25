package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
