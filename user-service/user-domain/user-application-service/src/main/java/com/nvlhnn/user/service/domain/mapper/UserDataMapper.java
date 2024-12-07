package com.nvlhnn.user.service.domain.mapper;

import com.nvlhnn.user.service.domain.dto.post.RegisterCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

    public User registerCommandToUser(RegisterCommand registerCommand) {
        return User.builder()
                .name(registerCommand.getName())
                .email(registerCommand.getEmail()) // Assuming Email class encapsulates email validation
                .password(registerCommand.getPassword())  // Will be hashed when set
                .build();
    }

    public UserResponse userToUserResponse(User user, String message) {
        return UserResponse.builder()
                .userId(user.getId().getValue())
                .isActive(user.isActive())
                .token(user.getToken())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .message(message)
                .build();
    }

    public User loginCommandToUser(RegisterCommand registerCommand) {
        return User.builder()
                .email(registerCommand.getEmail()) // Assuming Email class encapsulates email validation
                .password(registerCommand.getPassword())  // Will be hashed when set
                .build();
    }

}
