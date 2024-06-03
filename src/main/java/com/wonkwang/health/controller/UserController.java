package com.wonkwang.health.controller;

import com.wonkwang.health.domain.User;
import com.wonkwang.health.dto.ResponseDTO;
import com.wonkwang.health.dto.ResponseEntityBuilder;
import com.wonkwang.health.dto.UserDTO;
import com.wonkwang.health.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import static com.wonkwang.health.dto.ResponseEntityBuilder.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Long>> login(@RequestBody UserDTO userDTO, HttpServletRequest request) {

        User findUser = userService.login(userDTO);

        request.getSession().invalidate(); //기존 세션 파기
        HttpSession session = request.getSession(true); //세션이 없으면 생성
        session.setAttribute("userId", findUser.getId());
        session.setAttribute("role", findUser.getRole());
        session.setMaxInactiveInterval(10); //세션 유지 시간 (초)

        log.info("로그인 성공 {}",findUser.getId());

        return build("로그인을 성공했습니다.", OK, findUser.getId());
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseDTO<?>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);//세션이 없으면 null return

        if (session != null){
            session.invalidate();
            return build("로그아웃을 성공했습니다.", OK);
        } else {
            return build("현재 로그인 상태가 아닙니다.", BAD_REQUEST);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseDTO<Long>> checkState(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @SessionAttribute(required = false) Long userId) {
        // JSESSIONID 쿠키 확인
        boolean sessionIdExists = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    sessionIdExists = true;
                    break;
                }
            }
        }
        if (!sessionIdExists) {
            return build("애초에 세션이 존재하지 않음", BAD_REQUEST, null);
        }

        // 세션이 만료되었는지 확인
        HttpSession session = request.getSession(false);
        if (session == null || userId == null) {

            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(0); // 쿠키 만료 시간 설정 (0으로 설정하여 즉시 삭제)
            response.addCookie(cookie);
            return build("세션이 만료되었습니다.", HttpStatus.UNAUTHORIZED, null);
        }

        // 세션이 유효한 경우
        return build("검증성공", HttpStatus.OK, userId);
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseDTO<Long>> test(@SessionAttribute("userId") Long userId) {


        return build("검증성공", OK, userId);
    }
}
