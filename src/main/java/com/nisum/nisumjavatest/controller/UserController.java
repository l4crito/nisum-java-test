package com.nisum.nisumjavatest.controller;

import com.nisum.nisumjavatest.dto.UserDto;
import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear usuario",
            description = "Si en el request se asigna un UUID se creara el usuario con este id, caso contrario se asignara uno aleatorio.")
    public User createUser(@Valid @RequestBody UserDto user) {
        User newUser = userService.createUser(user);
        newUser.setPassword("");
        return newUser;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modificar usuario",
            description = "Modifica el usuario existen en base al UUID del request, valida primero la existencia del usuario.")
    public User updateUser(@Valid @RequestBody UserDto user) {
        User updatedUser = userService.updateUser(user);
        updatedUser.setPassword("");
        return updatedUser;
    }

    @GetMapping()
    @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios y sus teléfonos.")
    public List<User> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> {
                    user.setPassword("");
                    return user;
                }).collect(Collectors.toList());
    }

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario en base a su UUID, valida primero la existencia del usuario.")
    public void deleteUser(@PathVariable UUID id) throws BusinessException {
        userService.deleteUser(id);
    }

}