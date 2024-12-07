package com.nvlhnn.user.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;

import java.util.Optional;

public interface UserDomainService {

    UserCreatedEvent createUser(User user, DomainEventPublisher<UserCreatedEvent> publisher);

    boolean validateUserCredentials(User user, String password);

    String generateJwtToken(User user, String secretKey);
}
