package com.nvlhnn.user.service.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Builder
public class LoginCommand {

    @NotNull
    @Email
    private final String email;

    @NotNull
    private final String password;
}
