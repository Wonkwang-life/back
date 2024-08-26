package com.wonkwang.health.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String position;
    private String phoneNumber;
    private String name;

    @Builder
    public Staff(String position, String phoneNumber, String name) {
        this.id = null;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }
}
