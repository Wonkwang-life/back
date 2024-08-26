package com.wonkwang.health.dto;

import com.wonkwang.health.domain.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {

    private Long id;
    private String position;
    private String phoneNumber;
    private String name;

    public StaffDTO(Staff staff) {
        id = staff.getId();
        position = staff.getPosition();
        phoneNumber = staff.getPhoneNumber();
        name = staff.getName();
    }

}
