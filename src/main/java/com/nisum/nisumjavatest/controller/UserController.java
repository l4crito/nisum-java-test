package com.nisum.nisumjavatest.controller;

import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
       return userService.createUser(user);
    }
    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) throws BusinessException {
        userService.deleteUser(id);
    }


}