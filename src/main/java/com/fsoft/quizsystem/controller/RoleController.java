package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.mapper.RoleMapper;
import com.fsoft.quizsystem.object.dto.response.RoleResponse;
import com.fsoft.quizsystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    ResponseEntity<?> getAllRoles() {
        List<RoleResponse> responses = roleService.findAllRoles().stream()
                                                  .map(roleMapper::entityToRoleResponse)
                                                  .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
        RoleResponse response = roleMapper.entityToRoleResponse(roleService.findRoleById(id));

        return ResponseEntity.ok(response);
    }
}
