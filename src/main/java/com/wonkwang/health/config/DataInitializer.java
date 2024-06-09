package com.wonkwang.health.config;

import com.wonkwang.health.domain.User;
import com.wonkwang.health.dto.UserDTO;
import com.wonkwang.health.repository.UserRepository;
import com.wonkwang.health.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        List<User> all = userRepository.findAll();
        if (all.isEmpty()){
            userService.join(new UserDTO("admin","1234")); //테스트용 데이터
        }

    }
}
