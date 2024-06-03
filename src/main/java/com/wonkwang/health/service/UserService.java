package com.wonkwang.health.service;

import com.wonkwang.health.domain.Role;
import com.wonkwang.health.domain.User;
import com.wonkwang.health.dto.UserDTO;
import com.wonkwang.health.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public User login(UserDTO userDTO) {
        User findUser = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다."));

        if (!passwordEncoder.matches(userDTO.getPassword(), findUser.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return findUser;
    }
    
    //관리자만 실행하는 메소드
    public void join(UserDTO userDTO) {
        Optional<User> findUser = userRepository.findByUsername(userDTO.getUsername());
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 있는 회원입니다.");
        }

        User user = new User(null, userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), Role.ADMIN);
        userRepository.save(user);
    }

}
