package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.BaseEntity;
import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.UserRole;

public class User extends BaseEntity<UserId> {

    private String name;
    private String email;
    private UserRole role; // Enum representing role type (e.g., SUPER_ADMIN, WAREHOUSE_ADMIN)
    private boolean isActive;

    private User(Builder builder) {
        super.setId(builder.userId);
        name = builder.name;
        email = builder.email;
        role = builder.role;
        isActive = builder.isActive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isSuperAdmin() {
        return UserRole.SUPER_ADMIN.equals(this.role);
    }

    public boolean isWarehouseAdmin() {
        return UserRole.WAREHOUSE_ADMIN.equals(this.role);
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public static final class Builder {
        private UserId userId;
        private String name;
        private String email;
        private UserRole role;
        private boolean isActive;

        private Builder() {
        }

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

        public User build() {
            return new User(this);
        }
    }
}
