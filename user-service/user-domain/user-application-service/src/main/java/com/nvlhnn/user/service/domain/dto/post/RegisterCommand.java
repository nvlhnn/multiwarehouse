package com.nvlhnn.user.service.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Builder
public class RegisterCommand {

    @NotNull
    @Size(min = 2, max = 100)
    private final String name;

    @NotNull
    @Email
    private final String email;

    @NotNull
    @Size(min = 6)
    private final String password;
}
