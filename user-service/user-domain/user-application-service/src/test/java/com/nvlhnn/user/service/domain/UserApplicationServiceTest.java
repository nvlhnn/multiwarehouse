package com.nvlhnn.user.service.domain;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.user.service.domain.dto.post.LoginCommand;
import com.nvlhnn.user.service.domain.dto.post.RegisterCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;
import com.nvlhnn.user.service.domain.exception.UserDomainException;
import com.nvlhnn.user.service.domain.mapper.UserDataMapper;
import com.nvlhnn.user.service.domain.ports.input.service.UserApplicationService;
import com.nvlhnn.user.service.domain.ports.output.repository.UserRepository;
import com.nvlhnn.user.service.domain.ports.output.message.publisher.UserCreatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserApplicationService.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = UserTestConfiguration.class)
public class UserApplicationServiceTest {

    @Autowired
    private UserApplicationService userApplicationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserCreatedEventPublisher userCreatedEventPublisher;

    @MockBean
    private PasswordEncoder passwordEncoder; //

    @Autowired
    private UserDataMapper userDataMapper;

    private RegisterCommand registerCommand;
    private RegisterCommand registerCommandExistingEmail;
    private LoginCommand loginCommand;
    private LoginCommand loginCommandWrongPassword;

    private final UUID USER_ID = UUID.randomUUID();
    private final String USER_NAME = "John Doe";
    private final String USER_EMAIL = "john.doe@example.com";
    private final String USER_PASSWORD = "securePassword";
    private final String SECRET_KEY = "mySecretKey";

    @BeforeEach
    public void init() {
        // Initialize RegisterCommand for successful registration
        registerCommand = RegisterCommand.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        // Initialize RegisterCommand for registration with existing email
        registerCommandExistingEmail = RegisterCommand.builder()
                .name("Jane Smith")
                .email(USER_EMAIL) // Same email as above
                .password("anotherPassword")
                .build();

        // Initialize LoginCommand for successful login
        loginCommand = LoginCommand.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        // Initialize LoginCommand with wrong password
        loginCommandWrongPassword = LoginCommand.builder()
                .email(USER_EMAIL)
                .password("wrongPassword")
                .build();

        // Mock PasswordEncoder behavior used by UserDomainServiceImpl
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn("hashedSecurePassword");
        when(passwordEncoder.matches(USER_PASSWORD, "hashedSecurePassword")).thenReturn(true);
        when(passwordEncoder.matches("wrongPassword", "hashedSecurePassword")).thenReturn(false);

        // Mocking userRepository.findByEmail for successful registration (email not existing)
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        // Mock userRepository.save to return the saved user
        User user = User.builder()
                .userId(new UserId(USER_ID))
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password("hashedSecurePassword") // Hashed password
                .isActive(true)
                .role(com.nvlhnn.domain.valueobject.UserRole.CUSTOMER)
                .build();
        user.initializeUser(); // Initialize user (hash password, set ID, etc.)

        // Ensure that both userId and id are set
        user.setId(new UserId(USER_ID)); // Assuming User has a setId(UserId id) method

        // Mock userRepository.save to return the saved user
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Test
    public void testRegisterUser_Success() {
        // Perform registration
        UserResponse response = userApplicationService.registerUser(registerCommand);

        // Assertions
        assertNotNull(response, "UserResponse should not be null");
        assertEquals(USER_NAME, response.getName(), "User name should match");
        assertEquals(USER_EMAIL, response.getEmail(), "User email should match");
        assertEquals("CUSTOMER", response.getRole(), "User role should be CUSTOMER");
        assertTrue(response.isActive(), "User should be active");
        assertNotNull(response.getToken(), "User token should not be null");
        assertEquals("User registered successfully", response.getMessage(), "Success message should match");

        // Verify that userRepository.save was called once
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(USER_NAME, capturedUser.getName(), "Captured user name should match");
        assertEquals(USER_EMAIL, capturedUser.getEmail(), "Captured user email should match");
        assertNotEquals(USER_PASSWORD, capturedUser.getPassword(), "Password should be hashed");

        // Verify that UserCreatedEventPublisher.publish was called once
        verify(userCreatedEventPublisher, times(1)).publish(any(UserCreatedEvent.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Mock userRepository.findByEmail to return an existing user
        User existingUser = User.builder()
                .userId(new UserId(USER_ID))
                .name("Existing User")
                .email(USER_EMAIL)
                .password("existingPassword")
                .isActive(true)
                .role(com.nvlhnn.domain.valueobject.UserRole.CUSTOMER)
                .build();
        existingUser.initializeUser();
        existingUser.setId(new UserId(USER_ID)); // Ensure id is set
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(existingUser));

        // Attempt to register with an existing email
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            userApplicationService.registerUser(registerCommandExistingEmail);
        });

        // Assertions
        assertEquals("User already exists.", exception.getMessage(), "Exception message should match");

        // Verify that userRepository.save was never called
        verify(userRepository, never()).save(any(User.class));

        // Verify that UserCreatedEventPublisher.publish was never called
        verify(userCreatedEventPublisher, never()).publish(any(UserCreatedEvent.class));
    }

    @Test
    public void testLoginUser_Success() {
        // Mock userRepository.findByEmail to return the user
        User user = User.builder()
                .userId(new UserId(USER_ID))
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD) // Hashed password
                .isActive(true)
                .role(com.nvlhnn.domain.valueobject.UserRole.CUSTOMER)
                .build();
        user.initializeUser();
        user.setId(new UserId(USER_ID)); // Ensure id is set
        user.generateToken("secretKey"); // Assuming this sets the token
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        // Perform login
        UserResponse response = userApplicationService.loginUser(loginCommand);

        // Log response for debugging
        log.info("Response Token: {}", response.getToken());

        // Assertions
        assertNotNull(response, "UserResponse should not be null");
        assertEquals(USER_NAME, response.getName(), "User name should match");
        assertEquals(USER_EMAIL, response.getEmail(), "User email should match");
        assertEquals("CUSTOMER", response.getRole(), "User role should be CUSTOMER");
        assertTrue(response.isActive(), "User should be active");
        assertNotNull(response.getToken(), "User token should not be null");
        assertEquals("User logged in successfully", response.getMessage(), "Success message should match");
    }

    @Test
    public void testLoginUser_WrongPassword() {
        // Mock userRepository.findByEmail to return the user
        User user = User.builder()
                .userId(new UserId(USER_ID))
                .name("John Doe")
                .email(USER_EMAIL)
                .password("hashedSecurePassword") // Hashed password
                .isActive(true)
                .role(com.nvlhnn.domain.valueobject.UserRole.CUSTOMER)
                .build();
        user.initializeUser();
        user.setId(new UserId(USER_ID)); // Ensure id is set
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        // Attempt to login with wrong password
        LoginCommand loginCommandWrongPassword = LoginCommand.builder()
                .email(USER_EMAIL)
                .password("wrongPassword")
                .build();

        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            userApplicationService.loginUser(loginCommandWrongPassword);
        });

        // Assertions
        assertEquals("Invalid credentials.", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testLoginUser_UserNotFound() {
        LoginCommand nonexistentLoginCommand = LoginCommand.builder()
                .email("nonexistent@example.com")
                .password("somePassword")
                .build();

        // Mock userRepository.findByEmail to return empty
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Attempt to login with non-existing email
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            userApplicationService.loginUser(nonexistentLoginCommand);
        });

        // Assertions
        assertEquals("User not found.", exception.getMessage(), "Exception message should match");
    }
}
