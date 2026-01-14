package com.example.quicksells.domain.item.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserInfoDto {

    private final String name;
    private final Long hopePrice;
    private final String description;
    private final String image;
    private final boolean status;
    private final Enum role;
    private final LocalDateTime createdAt;

}
