package com.shortthirdman.primekit.keycloak.adminclient.usermanagement.service;

import com.shortthirdman.primekit.keycloak.adminclient.config.KeycloakPropertiesConfig;
import com.shortthirdman.primekit.keycloak.adminclient.exception.DuplicateUserException;
import com.shortthirdman.primekit.keycloak.adminclient.exception.UserCreationException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.shortthirdman.primekit.keycloak.adminclient.usermanagement.dto.UserDto;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final KeycloakPropertiesConfig propertiesConfig;

    private UsersResource usersResourceInstance() {
        return keycloak.realm(propertiesConfig.getRealm()).users();
    }

    private UserDto mapToUserDto(UserRepresentation userRepresentation) {
        return UserDto.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }

    @Override
    public List<UserDto> getAll() {
        return usersResourceInstance().list().stream().map(this::mapToUserDto).toList();
    }

    @Override
    public List<UserDto> getByUsername(String username) {
        return usersResourceInstance()
                .search(username).stream().map(this::mapToUserDto).toList();
    }

    @Override
    public UserDto getById(String id) {
        var user = usersResourceInstance()
                .get(id)
                .toRepresentation();

        return mapToUserDto(user);
    }

    @Override
    public void assignRole(String userId, String roleName) {
        var roleRepresentation = keycloak.realm(propertiesConfig.getRealm()).roles().get(roleName).toRepresentation();
        usersResourceInstance()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRepresentation));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        var user = buildUserRepresentation(userDto);

        try (Response response = usersResourceInstance().create(user)) {
            int statusCode = response.getStatus();
            switch (statusCode) {
                case 201 -> log.info("User {} successfully created in Keycloak", userDto.username());
                case 409 -> {
                    log.error("Duplicate user {}", userDto.username());
                    throw new DuplicateUserException(userDto.username());
                }
                default -> {
                    log.error("Error creating user: status code {}", statusCode);
                    throw new UserCreationException(MessageFormat.format("Error creating user: status code {0}", statusCode));
                }
            }
        } catch (ProcessingException e) {
            log.error("Error creating user in Keycloak", e);
            throw new UserCreationException("Error creating user");
        }

        return userDto;
    }

    /**
     * @param userDto the user object
     * @return See {@link UserRepresentation}
     */
    private UserRepresentation buildUserRepresentation(UserDto userDto) {
        UserRepresentation userRepresentation  = new UserRepresentation();
        userRepresentation.setUsername(userDto.username());
        userRepresentation.setCredentials(Collections.singletonList(buildCredentialRepresentation(userDto.password())));
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(userDto.email());
        userRepresentation.setFirstName(userDto.firstName());
        userRepresentation.setLastName(userDto.lastName());
        userRepresentation.setEmailVerified(true);

        return userRepresentation ;
    }

    /**
     * @param password the password credential
     * @return See {@link CredentialRepresentation}
     */
    private CredentialRepresentation buildCredentialRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }
}
