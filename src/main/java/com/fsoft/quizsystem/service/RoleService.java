package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.mapper.RoleMapper;
import com.fsoft.quizsystem.object.dto.request.RoleRequest;
import com.fsoft.quizsystem.object.entity.Role;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any role with id " + id));
    }

    public Role createRole(RoleRequest requestBody) {
        Role role = roleMapper.roleRequestToEntity(requestBody);

        return roleRepository.save(role);
    }

    public Role updateRole(long id, RoleRequest requestBody) {
        Role role = this.findRoleById(id);
        roleMapper.updateEntity(role, requestBody);

        return roleRepository.save(role);
    }

    public void deleteRole(long id) {
        Role role = this.findRoleById(id);
        roleRepository.delete(role);
    }
}
