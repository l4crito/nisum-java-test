package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.dto.UserDto;
import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserDto user) throws BusinessException;

    List<User> getAllUsers();

    void deleteUser(UUID id) throws BusinessException;

    User updateUser(UserDto user) throws BusinessException;

}
