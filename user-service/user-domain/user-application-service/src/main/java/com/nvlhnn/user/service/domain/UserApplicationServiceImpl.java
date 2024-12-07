package com.nvlhnn.user.service.domain;

import com.nvlhnn.user.service.domain.dto.post.LoginCommand;
import com.nvlhnn.user.service.domain.dto.post.RegisterCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.ports.input.service.UserApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private final RegisterUserCommandHandler registerUserCommandHandler;
    private final LoginUserCommandHandler loginUserCommandHandler;

    public UserApplicationServiceImpl(RegisterUserCommandHandler registerUserCommandHandler,
                                      LoginUserCommandHandler loginUserCommandHandler) {
        this.registerUserCommandHandler = registerUserCommandHandler;
        this.loginUserCommandHandler = loginUserCommandHandler;
    }

    @Override
    public UserResponse registerUser(RegisterCommand registerCommand) {
        log.info("Registering user with email: {}", registerCommand.getEmail());
        return registerUserCommandHandler.registerUser(registerCommand);
    }

    @Override
    public UserResponse loginUser(LoginCommand loginCommand) {
        log.info("Logging in user with email: {}", loginCommand.getEmail());
        return loginUserCommandHandler.loginUser(loginCommand);
    }
}
