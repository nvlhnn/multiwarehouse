package com.nvlhnn.order.service.domain.ports.input.message.listener.user;


import com.nvlhnn.order.service.domain.dto.message.UserResponseMessage;

public interface UserServiceMessageListener {

    void onUserRegistered(UserResponseMessage userResponseMessage);
}
