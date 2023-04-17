package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;

import java.util.List;

public interface UserService {
    User createUser(User user) throws BusinessException;

    List<User> getAllUsers();
}
