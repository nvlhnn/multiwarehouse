package com.nvlhnn.product.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.entity.BaseEntity;
import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.UserRole;

public class User extends AggregateRoot<UserId> {

    private String name;
    private String email;
    private UserRole role;
    private boolean isActive;
    private String token; // JWT Token for authentication


    public User(UserId userId, String name, String email, UserRole role, boolean isActive, String token) {
        super.setId(userId);
        this.name = name;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getToken() {
        return token;
    }

    public boolean isWarehouseAdmin() {
        return (role == UserRole.WAREHOUSE_ADMIN || role == UserRole.SUPER_ADMIN);
    }

}
