package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.BaseEntity;
import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.UserRole;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;

import java.util.Date;
import java.util.UUID;

public class User extends BaseEntity<UserId> {

    private String name;
    private String email;
    private UserRole role;
    private boolean isActive;
    private String token; // JWT Token for authentication

    private User(Builder builder) {
        super.setId(builder.userId);
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
        this.isActive = builder.isActive;
        this.token = builder.token; // Token is set via builder
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeUser() {
        setId(this.getId());
        this.isActive = true;
        this.role = UserRole.CUSTOMER;
    }

    public void validateUser() {
        validateUsername();
        validateEmail();
    }


    public void updateUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
    }



    private void validateUsername() {
        if (name == null || name.trim().isEmpty()) {
            throw new WarehouseDomainException("Username cannot be null or empty.");
        }
    }

    private void validateEmail() {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {  // Simple email validation
            throw new WarehouseDomainException("Invalid email address.");
        }
    }

    public String getName() {
        return name;
    }

    public boolean isWarehouseAdmin() {
        return (role == UserRole.WAREHOUSE_ADMIN || role == UserRole.SUPER_ADMIN);
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
        return token;  // Return JWT token for the user
    }


    public static final class Builder {
        private UserId userId;
        private String name;
        private String email;
        private UserRole role;
        private boolean isActive;
        private String token; // JWT token for the user

        private Builder() {}

        public Builder userId(UserId userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder token(String token) {
            this.token = token; // Set the JWT token if needed
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
