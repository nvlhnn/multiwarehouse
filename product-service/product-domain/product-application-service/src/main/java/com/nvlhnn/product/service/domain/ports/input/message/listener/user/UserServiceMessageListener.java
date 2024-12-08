package com.nvlhnn.product.service.domain.ports.input.message.listener.user;


import com.nvlhnn.product.service.domain.dto.message.UserResponseMessage;

public interface UserServiceMessageListener {

    void onUserRegistered(UserResponseMessage userResponseMessage);
}
