package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.mapper.RoleMapper;
import com.fsoft.quizsystem.object.dto.request.RoleRequest;
import com.fsoft.quizsystem.object.dto.response.RoleResponse;
import com.fsoft.quizsystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PreAuthorize("hasAuthority('SYSTEM_ROLE_READ')")
    @GetMapping
    ResponseEntity<?> getAllRoles() {
        List<RoleResponse> responses = roleService.findAllRoles().stream()
                                                  .map(roleMapper::entityToRoleResponse)
                                                  .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAuthority('SYSTEM_ROLE_READ')")
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
        RoleResponse response = roleMapper.entityToRoleResponse(roleService.findRoleById(id));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SYSTEM_ROLE_CREATE')")
    @PostMapping
    ResponseEntity<?> createRole(@RequestBody @Valid RoleRequest body) {
        RoleResponse response = roleMapper.entityToRoleResponse(roleService.createRole(body));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SYSTEM_ROLE_UPDATE')")
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updateRole(@PathVariable("id") Long id, @RequestBody @Valid RoleRequest body) {
        RoleResponse response = roleMapper.entityToRoleResponse(roleService.updateRole(id, body));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SYSTEM_ROLE_DELETE')")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);

        return ResponseEntity.ok().build();
    }
}
