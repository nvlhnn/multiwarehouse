package com.nvlhnn.user.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.UserRole;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.user.service.domain.exception.UserDomainException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;

public class User extends AggregateRoot<UserId> {

    private String name;
    private String email;
    private String password; // The raw password that will be hashed
    private UserRole role;
    private boolean isActive;
    private String token; // JWT Token for authentication

    private User(Builder builder) {
        super.setId(builder.userId);
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password; // Store the raw password for hashing
        this.role = builder.role;
        this.isActive = builder.isActive;
        this.token = builder.token; // Token is set via builder
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeUser() {
        setId(new UserId(UUID.randomUUID()));
        this.isActive = true;

        // bypass role
        if (this.email.contains("admin")) {
            this.role = UserRole.SUPER_ADMIN;
        }else{
            this.role = UserRole.CUSTOMER;
        }
        setPassword(this.password);
    }

    public void validateUser() {
        validateUsername();
        validateEmail();
        validatePassword();
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new UserDomainException("Password must be at least 6 characters.");
        }
        this.password = hashPassword(password);  // Hash the password
    }

    public void validateLogin(String password) {
        if (!verifyPassword(password)) { // Verify the hashed password
            throw new UserDomainException("Invalid password.");
        }
    }

    public void generateToken(String secretKey) {
        this.token = createJwtToken(secretKey); // Generate JWT token when the user logs in
    }

    private String createJwtToken(String secretKey) {
        return Jwts.builder()
                .claim("userId", this.getId().getValue())
                .claim("role", this.role)
                .setSubject(this.email)  // Use email or userId as the subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365)))  // 1 year expiration
                .signWith(SignatureAlgorithm.HS512, secretKey)  // Use HS512 for signing with the secret key
                .compact();
    }

    public boolean verifyToken(String token, String secretKey) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true; // Valid token
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new UserDomainException("Token has expired.");
        } catch (Exception e) {
            return false; // Invalid token
        }
    }

    public void updateUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        setPassword(password);  // Hash new password
    }

    public void deactivateUser() {
        if (!isActive) {
            throw new UserDomainException("User is already deactivated.");
        }
        isActive = false;
    }

    private void validateUsername() {
        if (name == null || name.trim().isEmpty()) {
            throw new UserDomainException("Username cannot be null or empty.");
        }
    }

    private void validateEmail() {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {  // Simple email validation
            throw new UserDomainException("Invalid email address.");
        }
    }

    private void validatePassword() {
        if (password == null || password.length() < 6) {
            throw new UserDomainException("Password must be at least 6 characters.");
        }
    }

    // Hash password with BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());  // Generate salt and hash the password
    }

    // Verify hashed password
    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.password);  // Compare the provided password with the hashed password
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
        return token;  // Return JWT token for the user
    }

    public String getPassword() {
        return password;  // Return JWT token for the user
    }



    public static final class Builder {
        private UserId userId;
        private String name;
        private String email;
        private String password; // Store plain password that will be hashed
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

        public Builder password(String password) {
            this.password = password; // Store plain password to be hashed later
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
