package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.constant.SystemRole;
import com.fsoft.quizsystem.object.entity.Role;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleService {

    private final RoleRepository roleRepository;

    @PostConstruct
    private void init() {
        if (roleRepository.count() == 0) {
            List<Role> systemRoles = new ArrayList<>(Arrays.asList(
                    new Role(SystemRole.ADMIN),
                    new Role(SystemRole.INSTRUCTOR)
            ));

            roleRepository.saveAll(systemRoles);
        }
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any role with id " + id));
    }
}
