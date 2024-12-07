package com.nvlhnn.user.service.domain.rest;

import com.nvlhnn.user.service.domain.dto.post.LoginCommand;
import com.nvlhnn.user.service.domain.dto.post.RegisterCommand;
import com.nvlhnn.user.service.domain.dto.response.UserResponse;
import com.nvlhnn.user.service.domain.ports.input.service.UserApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = "application/vnd.api.v1+json")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid RegisterCommand registerCommand) {
        log.info("Registering user with email: {}", registerCommand.getEmail());
        UserResponse userResponse = userApplicationService.registerUser(registerCommand);
        log.info("User registered with id {}", userResponse.getUserId());
        return ResponseEntity.ok(userResponse);
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody @Valid LoginCommand loginCommand) {
        log.info("Logging in user with email: {}", loginCommand.getEmail());
        UserResponse userResponse = userApplicationService.loginUser(loginCommand);
        log.info("User logged in");
        return ResponseEntity.ok(userResponse);
    }
}
