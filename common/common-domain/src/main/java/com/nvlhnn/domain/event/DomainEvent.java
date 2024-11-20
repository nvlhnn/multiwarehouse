package com.nvlhnn.domain.event;

public interface DomainEvent<T> {

    void fire();
}
