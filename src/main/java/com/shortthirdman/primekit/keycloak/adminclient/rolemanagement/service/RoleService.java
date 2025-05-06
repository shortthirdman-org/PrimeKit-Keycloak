package com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.service;

import com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.dto.RoleDto;

import java.util.List;

public interface RoleService {

    void create(RoleDto roleDto);

    List<RoleDto> getAll();

    RoleDto getByName(String roleName);
}
