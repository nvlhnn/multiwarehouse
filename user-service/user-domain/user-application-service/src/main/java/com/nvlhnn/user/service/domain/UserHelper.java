package com.nvlhnn.user.service.domain;

import com.nvlhnn.user.service.domain.dto.post.LoginCommand;
import com.nvlhnn.user.service.domain.dto.post.RegisterCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;
import com.nvlhnn.user.service.domain.exception.UserDomainException;
import com.nvlhnn.user.service.domain.mapper.UserDataMapper;
import com.nvlhnn.user.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserHelper {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserDataMapper userDataMapper;

    public UserHelper(UserDomainService userDomainService,
                      UserRepository userRepository,
                      UserDataMapper userDataMapper) {
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
        this.userDataMapper = userDataMapper;
    }

    @Transactional
    public UserCreatedEvent persistUser(RegisterCommand registerCommand) {
        User user = userDataMapper.registerCommandToUser(registerCommand);

        // check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserDomainException("User already exists.");
        }

        UserCreatedEvent userCreatedEvent = userDomainService.createUser(user, null); // Event publishing can be added later
        saveUser(user);
        log.info("User with email: {} has been successfully registered", user.getEmail());
        return userCreatedEvent;
    }

    @Transactional
    public User fetchUser(LoginCommand loginCommand) {
        User user = userRepository.findByEmail(loginCommand.getEmail())
                .orElseThrow(() -> new UserDomainException("User not found."));

        if (!userDomainService.validateUserCredentials(user, loginCommand.getPassword())) {
            throw new UserDomainException("Invalid credentials.");
        }
        log.info("User with email: {} has successfully logged in", user.getEmail());

        return user;
    }

    private void saveUser(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            log.error("Could not save user!");
            throw new UserDomainException("Could not save user!");
        }
        log.info("User is saved with email: {}", savedUser.getEmail());
    }
}
