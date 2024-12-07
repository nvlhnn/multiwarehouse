package com.nvlhnn.user.service.domain;

import com.nvlhnn.user.service.domain.dto.post.LoginCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.mapper.UserDataMapper;
import com.nvlhnn.user.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginUserCommandHandler {

    private final UserHelper userHelper;
    private final UserDataMapper userDataMapper;

    public LoginUserCommandHandler(UserHelper userHelper,
                                   UserDataMapper userDataMapper) {
        this.userHelper = userHelper;
        this.userDataMapper = userDataMapper;
    }

    public UserResponse loginUser(LoginCommand loginCommand) {
        User user = userHelper.fetchUser(loginCommand);
        log.info("User with email: {} has successfully logged in", user.getEmail());

        return userDataMapper.userToUserResponse(user, "User logged in successfully");
    }
}
