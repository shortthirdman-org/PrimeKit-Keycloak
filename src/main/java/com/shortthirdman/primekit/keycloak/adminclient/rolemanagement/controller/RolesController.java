package com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.controller;

import com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.dto.RoleDto;
import com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/v1/roles"})
public class RolesController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll() {
        var roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody RoleDto roleDto) {
        roleService.create(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
