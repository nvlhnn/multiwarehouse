
package com.nvlhnn.user.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.user.service.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId warehouseId);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void delete(User user);
}
