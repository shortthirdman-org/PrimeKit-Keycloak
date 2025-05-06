package com.shortthirdman.primekit.keycloak.adminclient.usermanagement.service;

import com.shortthirdman.primekit.keycloak.adminclient.usermanagement.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    List<UserDto> getByUsername(String username);

    UserDto getById(String id);

    void assignRole(String userId, String roleName);

    UserDto createUser(UserDto userDto);
}
