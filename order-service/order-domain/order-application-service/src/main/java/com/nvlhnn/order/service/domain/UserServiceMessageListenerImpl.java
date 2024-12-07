package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.UserRegisteredSaga;
import com.nvlhnn.order.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.order.service.domain.ports.input.message.listener.user.UserServiceMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceMessageListenerImpl implements UserServiceMessageListener {

    private final UserRegisteredSaga userRegisteredSaga;

    public UserServiceMessageListenerImpl(UserRegisteredSaga userRegisteredSaga) {
        this.userRegisteredSaga = userRegisteredSaga;
    }

    @Override
    public void onUserRegistered(UserResponseMessage userResponseMessage) {
        log.info("Received UserRegistered event for user id: {}", userResponseMessage.getUserId());
        userRegisteredSaga.process(userResponseMessage);
    }

}
