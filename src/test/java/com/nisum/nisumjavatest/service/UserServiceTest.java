package com.nisum.nisumjavatest.service;

import com.nisum.nisumjavatest.dto.UserDto;
import com.nisum.nisumjavatest.entity.Phone;
import com.nisum.nisumjavatest.entity.User;
import com.nisum.nisumjavatest.exception.BusinessException;
import com.nisum.nisumjavatest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserDto userDto;

    private User user;
    private User user2;
    private List<User> userList;


    private final String EMAIL = "test@test.com";
    private final String EMAIL2 = "other@test.com";
    private final String PASSWORD = "pass123";

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail(EMAIL2);
        user2.setPassword(PASSWORD);
        user2.setCreated(LocalDateTime.now());
        user2.setModified(LocalDateTime.now());
        user2.setLastLogin(LocalDateTime.now());

        Set<Phone> phones = new HashSet<>();
        phones.add(Phone.builder().number("123124").build());
        user.setPhones(new HashSet<>());

        userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);
        ReflectionTestUtils.setField(userServiceImpl, "passwordRegex", "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");

    }

    @Test
    @DisplayName("Create User - Success")
    public void createUserTest() throws BusinessException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userServiceImpl.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(EMAIL, createdUser.getEmail());
        assertTrue(createdUser.isActive());
    }

    @Test
    @DisplayName("Create User - Invalid Email")
    public void createUserInvalidEmailTest() {
        userDto.setEmail("invalidEmail");

        Executable executable = () -> userServiceImpl.createUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Correo inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Create User - Invalid Password")
    public void createUserInvalidPasswordTest() {
        userDto.setPassword("pass");

        Executable executable = () -> userServiceImpl.createUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Contraseña inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Create User - Email already registered")
    public void createUserAlreadyRegisteredEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Executable executable = () -> userServiceImpl.createUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("El correo ya está registrado", exception.getMessage());
    }

    @Test
    @DisplayName("Update User - Success")
    public void updateUserSuccessTest() throws BusinessException {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userServiceImpl.updateUser(userDto);

        assertNotNull(updatedUser);
        assertEquals(EMAIL, updatedUser.getEmail());
        assertTrue(updatedUser.getPassword().startsWith("$2a$"));
    }

    @Test
    @DisplayName("Update User - Invalid Email")
    public void updateUserInvalidEmailTest() {
        userDto.setEmail("invalidEmail");

        Executable executable = () -> userServiceImpl.updateUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Correo inválido", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Update User - Invalid Password")
    public void updateUserInvalidPasswordTest() {
        userDto.setPassword("pass");

        Executable executable = () -> userServiceImpl.updateUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Contraseña inválida", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Update User - User not found")
    public void updateUserUserNotFoundTest() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        Executable executable = () -> userServiceImpl.updateUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Usuario no encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Update User - Email already registered")
    public void updateUserAlreadyRegisteredEmailTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user2));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userDto.setEmail(EMAIL);
        userDto.setId(user.getId());

        Executable executable = () -> userServiceImpl.updateUser(userDto);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("El correo ya está registrado", exception.getMessage());
    }

    @Test
    @DisplayName("Get all users - Success")
    public void getAllUsersSuccessTest() {
        when(userRepository.findAllWithPhones()).thenReturn(userList);

        List<User> result = userServiceImpl.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(EMAIL, result.get(0).getEmail());
        assertEquals(EMAIL2, result.get(1).getEmail());
    }

    @Test
    @DisplayName("Get all users - Empty list")
    public void getAllUsersEmptyTest() {
        when(userRepository.findAllWithPhones()).thenReturn(new ArrayList<>());

        List<User> result = userServiceImpl.getAllUsers();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Delete User - Success")
    public void deleteUserTest() throws BusinessException {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userServiceImpl.deleteUser(id);

        verify(userRepository, atLeastOnce()).findById(any());
        verify(userRepository, atLeastOnce()).delete(any());

    }

    @Test
    @DisplayName("Delete User - User not found")
    public void deleteUserNotFoundTest() {
        UUID id = UUID.randomUUID();

        Executable executable = () -> userServiceImpl.deleteUser(id);

        BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Usuario no encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}

