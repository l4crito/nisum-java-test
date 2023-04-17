package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Value("${password.regex}")
    private String passwordRegex;

    public User createUser(User user) throws BusinessException {
        if (!isValidEmail(user.getEmail())) {
            throw new BusinessException("Correo inválido", HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(user.getPassword())) {
            throw new BusinessException("Contraseña inválida", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("El correo ya está registrado",HttpStatus.CONFLICT);
        }

        String token = UUID.randomUUID().toString();
        user.setToken(token);

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);

        user.setActive(true);

        User savedUser = userRepository.save(user);
        return savedUser;
    }
    public List<User> getAllUsers() {
        return userRepository.findAllWithPhones();
    }

    public void deleteUser(UUID id) throws BusinessException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

}

