package com.nvlhnn.user.service.dataaccess.user.adapter;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.user.service.dataaccess.user.mapper.UserDataAccessMapper;
import com.nvlhnn.user.service.dataaccess.user.repository.UserJpaRepository;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.ports.output.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserDataAccessMapper userDataAccessMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserDataAccessMapper userDataAccessMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userDataAccessMapper = userDataAccessMapper;
    }

    @Override
    public User save(User user) {
        return userDataAccessMapper.userEntityToUser(userJpaRepository
                .save(userDataAccessMapper.userToUserEntity(user)));
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return userJpaRepository.findById(userId.getValue())
                .map(userDataAccessMapper::userEntityToUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userDataAccessMapper::userEntityToUser);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(userDataAccessMapper::userEntityToUser)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(userDataAccessMapper.userToUserEntity(user));
    }
}
