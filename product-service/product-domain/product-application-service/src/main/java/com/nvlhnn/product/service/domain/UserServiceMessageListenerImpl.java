package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.product.service.domain.entity.User;
import com.nvlhnn.product.service.domain.exception.ProductDomainException;
import com.nvlhnn.product.service.domain.mapper.ProductDataMapper;
import com.nvlhnn.product.service.domain.ports.input.message.listener.user.UserServiceMessageListener;
import com.nvlhnn.product.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceMessageListenerImpl implements UserServiceMessageListener {

    private final UserRepository userRepository;
    private final ProductDataMapper productDataMapper;

    public UserServiceMessageListenerImpl( UserRepository userRepository, ProductDataMapper productDataMapper) {
        this.userRepository = userRepository;
        this.productDataMapper = productDataMapper;
    }

    @Override
    public void onUserRegistered(UserResponseMessage userResponseMessage) {
        User user = userRepository.save(productDataMapper.userResponseMessageToUser(userResponseMessage));
        if (user == null) {
            log.error("User is not saved with email: {}", user.getEmail());
            throw new ProductDomainException("User is not saved with email: " + user.getEmail());
        }

        log.info("User is saved with name: {}", user.getEmail());
    }

}
