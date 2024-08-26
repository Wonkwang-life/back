package com.wonkwang.health.service;

import com.wonkwang.health.domain.Staff;
import com.wonkwang.health.dto.StaffDTO;
import com.wonkwang.health.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final DiscordService discordService;

    public List<StaffDTO> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();
        return staffList.stream().map(StaffDTO::new)
                .collect(Collectors.toList());
    }

    public void addStaff(StaffDTO staffDTO) {
        staffRepository.save(Staff.builder()
                .position(staffDTO.getPosition())
                .phoneNumber(staffDTO.getPhoneNumber())
                .name(staffDTO.getName())
                .build());

        discordService.sendActivityMessage(staffDTO.toString() + " 직원 추가 완료.");
        log.info("{} 직원 추가 완료.", staffDTO.toString());
    }
}
