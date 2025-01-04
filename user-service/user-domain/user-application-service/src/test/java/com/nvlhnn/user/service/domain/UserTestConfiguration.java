package com.nvlhnn.user.service.domain;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test configuration for the User Service domain tests.
 * It mocks all the necessary dependencies using Mockito, except for UserDomainService.
 */
@SpringBootConfiguration
@TestConfiguration
@ComponentScan(basePackages = "com.nvlhnn.user.service.domain")
public class UserTestConfiguration {

    // Define PasswordEncoder bean if needed by UserDomainService
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // If you prefer to manually define UserDomainService, add it here
     @Bean
     public UserDomainService userDomainService() {
         return new UserDomainServiceImpl();
     }

    // Note: It's preferable to let Spring auto-detect UserDomainServiceImpl via @Service
}
