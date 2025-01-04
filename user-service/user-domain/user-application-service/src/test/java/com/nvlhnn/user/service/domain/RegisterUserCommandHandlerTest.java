package com.nvlhnn.user.service.domain;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.UserRole;
import com.nvlhnn.user.service.domain.dto.post.RegisterCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;
import com.nvlhnn.user.service.domain.exception.UserDomainException;
import com.nvlhnn.user.service.domain.mapper.UserDataMapper;
import com.nvlhnn.user.service.domain.ports.output.message.publisher.UserCreatedEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserCommandHandlerTest {

    @InjectMocks
    private RegisterUserCommandHandler registerUserCommandHandler;

    @Mock
    private UserHelper userHelper;

    @Mock
    private UserDataMapper userDataMapper;

    @Mock
    private UserCreatedEventPublisher userCreatedEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        RegisterCommand command = RegisterCommand.builder()
                .name("Charlie")
                .email("charlie@example.com")
                .password("password456")
                .build();

        User user = User.builder()
                .userId(new UserId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45")))
                .name("Charlie")
                .email("charlie@example.com")
                .password("password456")
                .role(UserRole.CUSTOMER)
                .build();

        UserCreatedEvent event = new UserCreatedEvent(user, ZonedDateTime.now(), userCreatedEventPublisher);

        when(userHelper.persistUser(command)).thenReturn(event);
        when(userDataMapper.userToUserResponse(user, "User registered successfully"))
                .thenReturn(UserResponse.builder()
                        .userId(user.getId().getValue())
                        .email(user.getEmail())
                        .name(user.getName())
                        .role(user.getRole().toString())
                        .isActive(user.isActive())
                        .token(user.getToken())
                        .message("User registered successfully")
                        .build());

        UserResponse response = registerUserCommandHandler.registerUser(command);

        assertNotNull(response, "UserResponse should not be null");
        assertEquals("User registered successfully", response.getMessage());
        verify(userHelper, times(1)).persistUser(command);
        verify(userCreatedEventPublisher, times(1)).publish(event);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        RegisterCommand command = RegisterCommand.builder()
                .name("Dave")
                .email("dave@example.com")
                .password("password789")
                .build();

        when(userHelper.persistUser(command)).thenThrow(new UserDomainException("User already exists."));

        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            registerUserCommandHandler.registerUser(command);
        });

        assertEquals("User already exists.", exception.getMessage());
        verify(userHelper, times(1)).persistUser(command);
        verify(userCreatedEventPublisher, times(0)).publish(any());
    }
}
