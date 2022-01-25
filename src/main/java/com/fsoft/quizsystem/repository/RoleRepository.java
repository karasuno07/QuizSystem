package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
