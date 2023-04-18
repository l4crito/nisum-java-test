package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.dto.UserDto;
import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.repository.UserRepository;
import com.nisum.nisumjavatest.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${password.regex}")
    private String passwordRegex;

    public User createUser(UserDto userDto) throws BusinessException {
        if (!isValidEmail(userDto.getEmail())) {
            throw new BusinessException("Correo inválido", HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(userDto.getPassword())) {
            throw new BusinessException("Contraseña inválida", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BusinessException("El correo ya está registrado",HttpStatus.CONFLICT);
        }
        User user = modelMapper.map(userDto,User.class);
        user.setId(userDto.getId() == null ? UUID.randomUUID() : userDto.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        String token = JwtUtil.generateToken(userDto.getEmail());
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
                .orElseThrow(() -> new BusinessException("Usuario no encontrado", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(UserDto userDto) throws BusinessException {
        if (!isValidEmail(userDto.getEmail())) {
            throw new BusinessException("Correo inválido", HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(userDto.getPassword())) {
            throw new BusinessException("Contraseña inválida", HttpStatus.BAD_REQUEST);
        }

        Optional<User> currentUser = userRepository.findById(userDto.getId());

        if (!currentUser.isPresent()) {
            throw new BusinessException("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        if ( userDto.getEmail()!=null && !userDto.getEmail().equals(currentUser.get().getEmail()) ) {
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new BusinessException("El correo ya está registrado",HttpStatus.CONFLICT);
            }
        }


        modelMapper.map(userDto, currentUser.get());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        currentUser.get().setPassword(passwordEncoder.encode(userDto.getPassword()));

        String token = JwtUtil.generateToken(userDto.getEmail());
        currentUser.get().setToken(token);
        currentUser.get().setModified(LocalDateTime.now());

        return userRepository.save(currentUser.get());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

}

