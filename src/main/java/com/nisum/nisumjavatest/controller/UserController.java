package com.nisum.nisumjavatest.controller;

import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.exception.ErrorMessage;
import com.nisum.nisumjavatest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            createdUser.setPassword(null); // remove password from response
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(new ErrorMessage(e.getMessage()));
        }
    }
}