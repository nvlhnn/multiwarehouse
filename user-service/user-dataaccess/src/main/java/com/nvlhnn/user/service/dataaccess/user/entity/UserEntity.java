package com.nvlhnn.user.service.dataaccess.user.entity;

import com.nvlhnn.domain.valueobject.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    private UUID id;

    private String email;
    private String name;
    private UserRole role;
    private String password;
    private Boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
