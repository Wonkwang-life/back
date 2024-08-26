package com.wonkwang.health.controller;

import com.wonkwang.health.domain.Role;
import com.wonkwang.health.dto.ResponseDTO;
import com.wonkwang.health.dto.ResponseEntityBuilder;
import com.wonkwang.health.dto.StaffDTO;
import com.wonkwang.health.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wonkwang.health.dto.ResponseEntityBuilder.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<StaffDTO>>> getAllStaff() {
        return build("직원 명단 리스트", OK, staffService.getAllStaff());
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> addStaff(StaffDTO staffDTO, @SessionAttribute Role role) {
        if (role != Role.ADMIN) {
            return build("권한 부족", FORBIDDEN);
        }
        staffService.addStaff(staffDTO);
        return build("직원 추가 성공", OK);
    }
}
