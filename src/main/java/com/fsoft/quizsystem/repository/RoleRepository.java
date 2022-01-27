package com.fsoft.quizsystem.repository;

import com.fsoft.quizsystem.object.constant.SystemRole;
import com.fsoft.quizsystem.object.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(SystemRole name);
}
