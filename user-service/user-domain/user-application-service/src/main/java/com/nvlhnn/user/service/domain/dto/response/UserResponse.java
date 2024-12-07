package com.nvlhnn.user.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class UserResponse {

    @NotNull
    private final UUID userId;

    @NotNull
    private final String name;

    @NotNull
    private final String email;

    @NotNull
    private final String role;

    @NotNull
    private final boolean isActive;

    @NotNull
    private final String token;

    @NotNull
    private String message;
}
