package com.shortthirdman.primekit.keycloak.adminclient.usermanagement.controller;

import com.shortthirdman.primekit.keycloak.adminclient.usermanagement.dto.UserDto;
import com.shortthirdman.primekit.keycloak.adminclient.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/v1/users"})
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        var users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}/assign/role/{roleName}")
    public ResponseEntity<?>  assignRoleToUser(@PathVariable String userId, @PathVariable String roleName){
        userService.assignRole(userId, roleName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
