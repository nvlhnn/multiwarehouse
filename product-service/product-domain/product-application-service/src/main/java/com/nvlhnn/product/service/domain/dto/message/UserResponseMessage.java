package com.nvlhnn.product.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseMessage {

    private final String id;
    private final String sagaId;
    private final String userId;
    private final String email;
    private final String name;
    private final String role;
    private final String token;
    private final boolean isActive;
    private final Instant eventTimestamp;

}
