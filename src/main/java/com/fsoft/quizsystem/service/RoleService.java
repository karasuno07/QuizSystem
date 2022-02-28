package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.mapper.RoleMapper;
import com.fsoft.quizsystem.object.entity.es.RoleES;
import com.fsoft.quizsystem.object.entity.jpa.Role;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.es.RoleEsRepository;
import com.fsoft.quizsystem.repository.jpa.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleEsRepository roleEsRepository;
    private final RoleMapper roleMapper;

    public List<Role> findAllRoles() {
        List<Role> roles;

        if (roleEsRepository.count() > 0) {
            roles = StreamSupport.stream(roleEsRepository.findAll().spliterator(), false)
                                 .map(roleMapper::esEntityToJpa)
                                 .collect(Collectors.toList());
        } else {
            roles = roleRepository.findAll();
        }

        return roles;
    }

    public Role findRoleById(Long id) {
        Optional<RoleES> optional = roleEsRepository.findById(id);

        if (optional.isPresent()) {
            return roleMapper.esEntityToJpa(optional.get());
        } else {
            return roleRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any role with id " + id));
        }
    }

}
