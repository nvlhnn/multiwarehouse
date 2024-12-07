package com.nvlhnn.user.service.domain.event;

import com.nvlhnn.domain.event.DomainEvent;
import com.nvlhnn.user.service.domain.entity.User;

import java.time.ZonedDateTime;

public abstract class UserEvent implements DomainEvent<User> {

    private final User user;
    private final ZonedDateTime createdAt;

    public UserEvent(User user, ZonedDateTime createdAt) {
        this.user = user;
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    // Abstract method to be implemented by subclasses to fire the event
    public abstract void fire();
}
