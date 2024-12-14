package com.nvlhnn.user.service.domain;

import com.nvlhnn.user.service.domain.mapper.UserDataMapper;
import com.nvlhnn.user.service.domain.ports.input.service.UserApplicationService;
import com.nvlhnn.user.service.domain.ports.output.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = UserTestConfiguration.class)
public class UserApplicationServiceTest {
    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    public void init(){
    }

    @Test
    public void testCreateOrder(){
    }
}
