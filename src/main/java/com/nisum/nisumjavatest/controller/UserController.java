package com.nisum.nisumjavatest.controller;

import com.nisum.nisumjavatest.dto.UserDto;
import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value= "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {

        return userService.getAllUsers().stream()
                .map(user -> {
                    user.setPassword("");
                    return user;
                }).collect(Collectors.toList());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto user) {
        User newUser = userService.createUser(user);
        newUser.setPassword("");
        return newUser;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody UserDto user) {
        User updatedUser = userService.updateUser(user);
        updatedUser.setPassword("");
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) throws BusinessException {
        userService.deleteUser(id);
    }


}