package com.wonkwang.health.config;

import com.wonkwang.health.dto.UserDTO;
import com.wonkwang.health.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    @Override
    public void run(String... args) throws Exception {
        userService.join(new UserDTO("admin","1234")); //테스트용 데이터
    }
}
