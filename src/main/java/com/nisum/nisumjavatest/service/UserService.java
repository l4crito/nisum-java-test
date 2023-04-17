package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws BusinessException {
        // Validar que el correo tenga el formato correcto
        if (!isValidEmail(user.getEmail())) {
            throw new BusinessException("Correo inválido", HttpStatus.BAD_REQUEST);
        }

        // Validar que la contraseña tenga el formato correcto
        if (!isValidPassword(user.getPassword())) {
            throw new BusinessException("Contraseña inválida", HttpStatus.BAD_REQUEST);
        }

        // Validar que el correo no esté registrado previamente
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("El correo ya está registrado",HttpStatus.CONFLICT);
        }

        // Generar un token aleatorio para el usuario
        String token = UUID.randomUUID().toString();
        user.setToken(token);

        // Establecer fechas de creación y modificación
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);

        // Establecer el estado de activo
        user.setActive(true);

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    private boolean isValidEmail(String email) {
        // Validar que el correo tenga el formato correcto utilizando una expresión regular
        // Se podría utilizar una biblioteca de validación de correos electrónicos en lugar de la expresión regular
        return email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
    }

    private boolean isValidPassword(String password) {
        // Validar que la contraseña tenga el formato correcto utilizando una expresión regular
        // El patrón de la expresión regular podría ser configurado en una propiedad
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
    }
}

