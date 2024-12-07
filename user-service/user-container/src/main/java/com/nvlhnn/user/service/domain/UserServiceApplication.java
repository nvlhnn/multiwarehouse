package com.nvlhnn.user.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.nvlhnn.user.service.dataaccess", "com.nvlhnn.dataaccess" })
@EntityScan(basePackages = { "com.nvlhnn.user.service.dataaccess", "com.nvlhnn.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.nvlhnn")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}