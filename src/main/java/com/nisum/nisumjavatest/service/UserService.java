package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user) throws BusinessException;

    List<User> getAllUsers();

    void deleteUser(UUID id);
}
