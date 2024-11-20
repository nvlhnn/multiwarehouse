package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Builder
public class User {

    @NotNull
    private final String name;

    @NotNull
    private final String email;

    @NotNull
    private final boolean isActive;
}
