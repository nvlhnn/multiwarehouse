package com.nvlhnn.user.service.domain;

import com.nvlhnn.user.service.domain.UserDomainService;
import com.nvlhnn.user.service.domain.UserDomainServiceImpl;
import com.nvlhnn.user.service.domain.ports.output.repository.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.nvlhnn")
public class UserTestConfiguration {


    @Bean
    public UserRepository customerRepository(){
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public UserDomainService orderDomainService(){
        return new UserDomainServiceImpl();
    }

}
