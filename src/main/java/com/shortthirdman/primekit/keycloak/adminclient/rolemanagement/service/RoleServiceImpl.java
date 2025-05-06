package com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.service;

import com.shortthirdman.primekit.keycloak.adminclient.config.KeycloakPropertiesConfig;
import com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final Keycloak keycloak;
    private final KeycloakPropertiesConfig propertiesConfig;

    private RolesResource rolesResourceInstance() {
        return keycloak.realm(propertiesConfig.getRealm()).roles();
    }

    @Override
    public void create(RoleDto roleDto) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleDto.name());
        role.setDescription(roleDto.description());

        rolesResourceInstance().create(role);
    }

    @Override
    public List<RoleDto> getAll() {
        var roleRepresentations = rolesResourceInstance().list();

        return roleRepresentations.stream().map(r -> new RoleDto(r.getName(), r.getDescription())).toList();
    }

    @Override
    public RoleDto getByName(String roleName) {
        var roleRepresentation = rolesResourceInstance().get(roleName).toRepresentation();

        return new RoleDto(roleRepresentation.getName(), roleRepresentation.getDescription());
    }
}
